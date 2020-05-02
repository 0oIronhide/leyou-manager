package cn.hp.item.mapper;

import cn.hp.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-17:59
 */
public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand (category_id, brand_id) values (#{categoryId}, #{brandId})")
    void insertBrandCategory(@Param("categoryId") Long categoryId, @Param("brandId") Long brandId);

    @Delete("delete from tb_category_brand where brand_id = #{id}")
    void deleteBrandCategory(Long id);

    @Select("select name from tb_brand where id = #{brandId}")
    String getBrandName(Long brandId);

    @Select("select b.id,b.name from tb_brand b LEFT JOIN tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> getBrandByCategoryId(Long cid);
}
