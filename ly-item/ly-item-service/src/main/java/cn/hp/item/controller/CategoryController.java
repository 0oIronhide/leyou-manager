package cn.hp.item.controller;

import cn.hp.item.pojo.Category;
import cn.hp.item.mapper.CategoryMapper;
import cn.hp.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-17:12
 */
@RequestMapping("category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("list")
    public ResponseEntity<List<Category>> list(@RequestParam("pid") Long parentId) {
        List<Category> categories = service.categoryList(parentId);
        if (categories != null && categories.size() > 0) {
            return ResponseEntity.ok(categories);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("bid/{id}")
    public ResponseEntity<List<Category>> getBrandCategory(@PathVariable("id") Long id) {
        List<Category> categories = service.getBrandCategory(id);
        if (categories == null || categories.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(categories);
    }

}
