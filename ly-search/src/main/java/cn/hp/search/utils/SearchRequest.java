package cn.hp.search.utils;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/09/20:14
 * @Description:
 */
//接收浏览器传过来的数据
public class SearchRequest {

    private String key;//搜索关键字
    private Integer page;//当前页
    private static final Integer DEFAULT_PAGE = 1;//默认第一页
    private static final Integer DEFAULT_SIZE = 20;//默认一页显示多少条
    private Map<String, String> filter;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;//1
        }
        return Math.max(page, DEFAULT_PAGE);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public static Integer getDefaultPage() {
        return DEFAULT_PAGE;
    }

    public static Integer getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}
