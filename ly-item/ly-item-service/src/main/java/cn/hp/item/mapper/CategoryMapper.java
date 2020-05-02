package cn.hp.item.mapper;

import cn.hp.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-17:15
 */
public interface CategoryMapper extends Mapper<Category> {

    @Select("select c.* from tb_category c LEFT JOIN tb_category_brand cb on c.id = cb.category_id where cb.brand_id = #{id}")
    List<Category> getBrandCategory(Long id);

    @Select("select name from tb_category where id = #{cid}")
    String getCategotyName(Long cid);
}
