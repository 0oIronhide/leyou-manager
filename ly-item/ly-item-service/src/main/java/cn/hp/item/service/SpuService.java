package cn.hp.item.service;

import cn.hp.item.mapper.*;
import cn.hp.item.pojo.*;
import cn.hp.utils.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-22:39
 */
@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public PageResult<SpuBo> page(Integer page, Integer rows, Boolean saleable, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable ? 1 : 0);
        }
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        List<Spu> spuList = spus.getResult();
        List<SpuBo> spuBoList = new ArrayList<>();
        spuList.forEach(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            String cids = "(" + spuBo.getCid1() + "," + spuBo.getCid2() + "," + spuBo.getCid3() + ")";
            List<String> names = categoryMapper.getCategotyName(cids);
            spuBo.setCname(StringUtils.join(names, "/"));
            spuBo.setBname(brandMapper.getBrandName(spuBo.getBrandId()));
            spuBoList.add(spuBo);
        });
        return new PageResult<>(spus.getTotal(), new Long(spus.getPages()), spuBoList);
    }

    @Transactional
    public Integer addOrUpdateSpu(SpuBo spuBo) {
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo, spu);
        Integer res = 0;
        Date date = new Date();
        spu.setLastUpdateTime(date);
        if (spuBo.getId() == null) {
            spu.setCreateTime(date);
            spu.setSaleable(true);
            spu.setValid(true);
            res = spuMapper.insertSelective(spu);
        } else {
            res = spuMapper.updateByPrimaryKeySelective(spu);
        }

        if (spuBo.getSpuDetail().getSpuId() == null) {
            spuBo.getSpuDetail().setSpuId(spu.getId());
            spuDetailMapper.insertSelective(spuBo.getSpuDetail());
        } else {
            spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        }

        List<Sku> skus = spuBo.getSkus();
        skus.forEach(sku -> {
            Stock stock = new Stock();
            sku.setLastUpdateTime(date);
            if (sku.getId() == null) {
                sku.setSpuId(spu.getId());
                sku.setCreateTime(date);
                skuMapper.insertSelective(sku);

                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insertSelective(stock);
            } else {
                skuMapper.updateByPrimaryKeySelective(sku);

                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.updateByPrimaryKeySelective(stock);
            }
        });
        return res;
    }

    public SpuDetail getSpuDetail(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public List<Sku> getSpuSkus(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        return skuMapper.select(sku);
    }

}
