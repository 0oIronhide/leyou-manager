package cn.hp.item.service;

import cn.hp.item.mapper.BrandMapper;
import cn.hp.item.pojo.Brand;
import cn.hp.utils.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Ironhide
 * @create 2020-04-28-18:04
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> pageQuery(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("name", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + (desc ? " DESC" : " ASC")); //order by id desc
        }
        Page<Brand> brandPage = (Page<Brand>) brandMapper.selectByExample(example);
        return new PageResult<>(brandPage.getTotal(), new Long(brandPage.getPages()), brandPage);
    }

    @Transactional
    public Integer addBrand(Brand brand, List<Long> cids) {
        Integer res = brandMapper.insertSelective(brand);
        cids.forEach(cid -> {
            brandMapper.insertBrandCategory(cid, brand.getId());
        });
        return res;
    }

    @Transactional
    public Integer updateBrand(Brand brand, List<Long> cids) {
        brandMapper.deleteBrandCategory(brand.getId());
        cids.forEach(cid -> {
            brandMapper.insertBrandCategory(cid, brand.getId());
        });
        return brandMapper.updateByPrimaryKeySelective(brand);
    }
}
