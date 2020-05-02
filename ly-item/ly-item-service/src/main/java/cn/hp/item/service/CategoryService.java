package cn.hp.item.service;

import cn.hp.item.mapper.CategoryMapper;
import cn.hp.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
