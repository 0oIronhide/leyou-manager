package cn.hp.item.api;

import cn.hp.item.pojo.Sku;
import cn.hp.item.pojo.SpuBo;
import cn.hp.item.pojo.SpuDetail;
import cn.hp.utils.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpuApi {
    /**
     * 根据查询条件分页查询商品信息
     *
     * @param key      搜索条件
     * @param saleable 上下架
     * @param page     当前页
     * @param rows     每页大小
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "saleable", required = false) Boolean saleable,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 通过 spuId 查询 SpuDetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 通过 spuId 查询 Sku 集合
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);
}

