package cn.hp.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {
    /**
     * 根据商品分类 id，查询商品分类名称
     *
     * @param ids
     * @return
     */
    @GetMapping("/names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}