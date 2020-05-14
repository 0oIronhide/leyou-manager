package cn.hp.user.service;

import cn.hp.user.mapper.UserMapper;
import cn.hp.user.pojo.User;
import cn.hp.utils.CodecUtils;
import cn.hp.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Boolean checkData(String data, Integer type) {
        //如果type为1表示校验用户名，2，表示手机号
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
        }
        //根据上面的条件进行查询，如果查到了值则也只会有一个值，所以1!=1的结果为false
        //查询到了返回false，没查询到返回true
        return userMapper.selectCount(record) != 1;
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    /**
     * 提出发送验证码的需求，把需求发送给消息中间件
     *
     * @param phone
     * @return
     */
    public Boolean sendVerifyCode(String phone) {
        //指定长度生成一个随机值
        String code = NumberUtils.generateCode(5);
        try {
            // 发送短信...
            //存入到redis中，并指定有效周期为5分钟
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            //logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }

    /**
     * 注册业务，进来后首先要验证验证码
     */
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            return false;
        }
        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());
        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 添加到数据库
        boolean flag = this.userMapper.insertSelective(user) == 1;

        // 如果注册成功，删除redis中的code
        if (flag) {
            this.redisTemplate.delete(key);
        }
        return flag;
    }

    public User queryUser(String username, String password) {
        // 查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            return null;
        }
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            return null;
        }
        // 用户名密码都正确
        return user;
    }
}