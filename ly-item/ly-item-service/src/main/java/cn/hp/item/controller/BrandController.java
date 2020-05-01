package cn.hp.item.controller;

import cn.hp.item.pojo.Brand;
import cn.hp.item.service.BrandService;
import cn.hp.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-18:04
 */
@RequestMapping("brand")
@RestController
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> pageQuery(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ) {
        PageResult<Brand> brandPageResult = this.service.pageQuery(page, rows, sortBy, desc, key);
        //判断分页数据不为空
        if (brandPageResult != null && null != brandPageResult.getItems() && 0 != brandPageResult.getItems().size()) {
            return ResponseEntity.ok(brandPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        if (service.addBrand(brand, cids) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        if (service.updateBrand(brand, cids) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
