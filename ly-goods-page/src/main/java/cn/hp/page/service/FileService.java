package cn.hp.page.service;


import cn.hp.page.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

@Service
public class FileService {

    @Value("${ly.thymeleaf.destPath}")
    private String destPath;//D:/nginx-1.14.2/html/item
    @Autowired
    private TemplateEngine templateEngine; //resttemplate
    @Autowired
    private PageService pageService;

    //判断文件夹下有没有指定html
    public boolean exist(Long spuId) {
        File file = new File(destPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(destPath, spuId + ".html");//113.html
        return file1.exists();//第一次false
    }

    public void syncCreateHtml(Long spuId) {
        //使用多线程，调用产生静态页面方法
        ThreadUtils.execute(() -> {
            createHtml(spuId);
        });
    }

    public void createHtml(Long spuId) {
        //创建上下文对象
        Context context = new Context();
        // 调用之前写好的加载数据方法
        context.setVariables(pageService.loadData(spuId));
        // 准备文件路径
        File file = new File(destPath, spuId + ".html");

        //创建一个输出流对象
        try {
            PrintWriter pr = new PrintWriter(file, "UTF-8");
            templateEngine.process("item", context, pr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteHtml(Long id) {
        File file = new File(destPath, id + ".html");
        file.deleteOnExit();
    }
}
