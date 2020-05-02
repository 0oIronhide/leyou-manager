package cn.hp.item.controller;

import cn.hp.item.pojo.Sku;
import cn.hp.item.pojo.Spu;
import cn.hp.item.pojo.SpuBo;
import cn.hp.item.pojo.SpuDetail;
import cn.hp.item.service.SpuService;
import cn.hp.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.Beans;
import java.util.List;

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
        if (service.addOrUpdateSpu(spuBo) > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * 查询商品detail信息
     *
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> getSpuDetail(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = service.getSpuDetail(spuId);
        if (spuDetail == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 查询商品的sku信息
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> getSpuSkus(@RequestParam("id") Long spuId) {
        List<Sku> skus = service.getSpuSkus(spuId);
        if (skus == null || skus.size() == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 修改商品信息
     *
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateSpu(@RequestBody SpuBo spuBo) {
        if (service.addOrUpdateSpu(spuBo) > 0) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}
