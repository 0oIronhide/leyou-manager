package cn.hp.item.mapper;

import cn.hp.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Delete;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Ironhide
 * @create 2020-04-30-10:17
 */
public interface SpecParamMapper extends Mapper<SpecParam> {

    @Delete("delete from tb_spec_param where group_id = #{groupId}")
    void deleteGroupParam(Long groupId);

}
