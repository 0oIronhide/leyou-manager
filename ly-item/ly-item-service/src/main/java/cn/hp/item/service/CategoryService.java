package cn.hp.item.service;

import cn.hp.item.mapper.CategoryMapper;
import cn.hp.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-17:20
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper mapper;

    public List<Category> categoryList(Long parentId) {
        Category category = new Category();
        category.setParentId(parentId);
        return mapper.select(category);
    }

    public List<Category> getBrandCategory(Long id) {
        return mapper.getBrandCategory(id);
    }

    public List<String> queryCategoryNamesByIds(List<Long> ids) {
        List<String> names = new ArrayList<>();
        ids.forEach(id -> {
            names.add(mapper.getCategotyName(id));
        });
        return names;
    }

    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.mapper.selectByPrimaryKey(id);
        Category c2 = this.mapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.mapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}
