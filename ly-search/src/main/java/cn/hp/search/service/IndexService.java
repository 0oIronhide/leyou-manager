package cn.hp.search.service;

import cn.hp.item.pojo.*;
import cn.hp.search.client.CategoryClient;
import cn.hp.search.client.SpecificationClient;
import cn.hp.search.client.SpuClient;
import cn.hp.search.repository.GoodsRepository;
import cn.hp.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexService {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpuClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;


    public Goods buildGoods(SpuBo spuBo) {
        //SpuBo-Goods
        Goods goods = new Goods();
        //复制
        BeanUtils.copyProperties(spuBo, goods);


        //根据spu的分类id 查询分类名称
        List<String> names = categoryClient.queryNamesByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
        //拼接all包含标题，分类
        String all = spuBo.getTitle() + " " + StringUtils.join(names, " ");
        //华为 G9 Plus 32GB 手机 手机通讯 手机
        goods.setAll(all);

        //根据spu的id查询sku
        List<Sku> skus = goodsClient.querySkusBySpuId(spuBo.getId());

        List<Map<String, Object>> list = new ArrayList<>();
        List<Long> prices = new ArrayList<>();

        for (Sku sku : skus) {

            //把sku的价格放入list
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            list.add(map);
        }

        //list变成json字符串
        goods.setSkus(JsonUtils.serialize(list));
        goods.setPrice(prices);
        //specs
        Map<String, Object> specsMap = getSpecs(spuBo);
        goods.setSpecs(specsMap);
        return goods;
    }

    //根据spu查询规格参数
    private Map<String, Object> getSpecs(SpuBo spuBo) {
        Map<String, Object> specs = new HashMap<String, Object>();

        //查询spudetial
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuBo.getId());

        //查询规格参数
        List<SpecParam> specParams = specClient.querySpecParams(null, spuBo.getCid3(), true, null);

        //{"1":"1其它","2":"1G9青春版（全网通版）","3":"12016","5":"1143","6":"1其它","7":"Android","8":"骁龙（Snapdragon)","9":"骁龙617（msm8952）","10":"八核","11":1.5,"14":5.2,"15":"1920*1080(FHD)","16":800,"17":1300,"18":3000}
        //把通用的规格参数变成map结构
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });

        //{"4":["1白色","1金色"],"12":["3GB"],"13":["16GB"]}
        //把特有的规格参数变成map结构
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
        //7	76	3	操作系统	0		1	1
        //8	76	4	CPU品牌	0		1	1

        for (SpecParam specParam : specParams) {
            Long id = specParam.getId();//10
            String name = specParam.getName();//CPU核数
            Object value = null;
            //通用规格参数
            if (specParam.getGeneric()) {
                value = genericMap.get(id);//"八核"
                //如果是数值类型，分断
                if (null != value && specParam.getNumeric()) {
                    //把数字变成分段
                    value = this.chooseSegment(value.toString(), specParam);

                }

                //特有规格参数
            } else {
                value = specialMap.get(id);

            }
            if (null == value) {
                value = "其他";
            }
            specs.put(name, value);//CPU核数 八核


        }
        return specs;


    }


    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {//segment:1000-2000
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();//添加单位
                }
                break;
            }
        }
        return result;
    }

    public void createIndex(Long id) {
        Spu spu = this.goodsClient.querySpuById(id);
        SpuBo spuBo = new SpuBo();
        BeanUtils.copyProperties(spu, spuBo);
        Goods goods = this.buildGoods(spuBo);
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}
