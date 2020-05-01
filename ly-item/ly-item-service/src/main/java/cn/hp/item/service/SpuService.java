package cn.hp.item.service;

import cn.hp.item.mapper.BrandMapper;
import cn.hp.item.mapper.CategoryMapper;
import cn.hp.item.mapper.SpuMapper;
import cn.hp.item.pojo.Spu;
import cn.hp.item.pojo.SpuBo;
import cn.hp.utils.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-22:39
 */
@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuBo> page(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable ? 1 : 0);
        }
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        List<Spu> spuList = spus.getResult();
        List<SpuBo> spuBoList = new ArrayList<>();
        spuList.forEach(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            String cids = "(" + spuBo.getCid1() + "," + spuBo.getCid2() + "," + spuBo.getCid3() + ")";
            List<String> names = categoryMapper.getCategotyName(cids);
            spuBo.setCname(StringUtils.join(names, "/"));
            spuBo.setBname(brandMapper.getBrandName(spuBo.getBrandId()));
            spuBoList.add(spuBo);
        });
        return new PageResult<>(spus.getTotal(), new Long(spus.getPages()), spuBoList);
    }
}
