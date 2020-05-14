package cn.hp.page.service;

import cn.hp.item.pojo.*;
import cn.hp.page.client.GoodsClient;
import cn.hp.page.client.SpecClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/11/16:39
 * @Description:
 */
@Service
public class PageService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;

    public Map<String, Object> loadData(Long spuId) {

        Map<String, Object> map = new HashMap<>();
        //根据spu的id查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        map.put("spu", spu);

        //根据spuid查询商品详情
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spuId);
        map.put("spuDetail", spuDetail);
        //根据spuid查询sku
        List<Sku> skus = goodsClient.querySkusBySpuId(spuId);
        map.put("skus", skus);

        //根据spu的三级分类查询 特有的规格参数
        List<SpecParam> specParams = specClient.querySpecParams(null, spu.getCid3(), null, false);
        Map<Long, Object> spMap = new HashMap<>();
        for (SpecParam s : specParams) {
            //把规格参数id 和规格参数名称放入map
            spMap.put(s.getId(), s.getName());
        }

        //查询规格组
        map.put("specParams", spMap);
        //需要根据分类查询规格组，同时查询组内的参数
        List<SpecGroup> specGroups = specClient.querySpecGroups(spu.getCid3());
        map.put("specGroups", specGroups);
        return map;
    }
}
