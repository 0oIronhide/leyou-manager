package cn.hp.item.api;

import cn.hp.item.pojo.Brand;
import cn.hp.utils.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {
    /**
     * 根据品牌 id 查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id") Long id);

    @GetMapping("page")
    public PageResult<Brand> pageQuery(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows", defaultValue = "10") Integer rows,
                                       @RequestParam(value = "sortBy", required = false) String sortBy,
                                       @RequestParam(value = "desc", required = false) Boolean desc,
                                       @RequestParam(value = "key", required = false) String key);

    @GetMapping("cid/{id}")
    public List<Brand> queryBrandByCategory(@PathVariable("id") Long cid);

}