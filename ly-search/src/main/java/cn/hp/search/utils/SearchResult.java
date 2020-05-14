package cn.hp.search.utils;

import cn.hp.item.pojo.Brand;
import cn.hp.item.pojo.Category;
import cn.hp.utils.PageResult;
import com.leyou.search.pojo.Goods;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/10/13:38
 * @Description:
 */
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories; //商品分类过滤条件
    private List<Brand> brands; //商品品牌过滤条件

    private List<Map<String, Object>> specs;//规格参数过滤条件

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }
}
