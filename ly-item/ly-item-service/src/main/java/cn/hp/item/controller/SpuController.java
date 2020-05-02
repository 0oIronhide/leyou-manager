package cn.hp.item.controller;

import cn.hp.item.pojo.Spu;
import cn.hp.item.pojo.SpuBo;
import cn.hp.item.service.SpuService;
import cn.hp.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.Beans;

/**
 * @author Ironhide
 * @create 2020-04-28-22:40
 */
@RestController
public class SpuController {

    @Autowired
    private SpuService service;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> page(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "10") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ) {
        PageResult<SpuBo> spuPageResult = service.page(page, rows, saleable, key);
        if (spuPageResult != null && null != spuPageResult.getItems() && 0 != spuPageResult.getItems().size()) {
            return ResponseEntity.ok(spuPageResult);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 新增spu商品、spudetail、sku列表
     *
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> addSpu(@RequestBody SpuBo spuBo) {
        if (service.addSpu(spuBo) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
