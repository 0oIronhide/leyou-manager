package cn.hp.page.controller;

import cn.hp.page.service.FileService;
import cn.hp.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/11/16:37
 * @Description:
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;
    @Autowired
    private FileService fileServcie;

    //根据spu的id对应展示，详情页面
    @GetMapping("item/{spuId}.html")
    public String toPage(@PathVariable("spuId") Long spuId, Model model) {
        Map<String, Object> map = pageService.loadData(spuId);
        //根据spu的id查询所有的前台需要的数据，并封装到map中返回
        model.addAllAttributes(map);
        //存在吗？如果不存在则给你创建一个静态页面
        if (!fileServcie.exist(spuId)) {
            fileServcie.syncCreateHtml(spuId);
        }
        return "item";
    }

}
