package cn.hp.search.service;


import cn.hp.item.pojo.Brand;
import cn.hp.item.pojo.Category;
import cn.hp.item.pojo.SpecParam;
import cn.hp.search.client.BrandClient;
import cn.hp.search.client.CategoryClient;
import cn.hp.search.client.SpecificationClient;
import cn.hp.search.repository.GoodsRepository;
import cn.hp.search.utils.SearchRequest;
import cn.hp.search.utils.SearchResult;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/09/19:50
 * @Description:
 */
@Service
public class SearchService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private BrandClient brandClient;

    public SearchResult search(SearchRequest searchRequest) {
        String key = searchRequest.getKey();//前台搜索的key
        if (StringUtils.isBlank(key)){
            return null;
        }
        //自定义查询
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //调用方法，返回一个查询条件对象
           QueryBuilder query=buildBaseicQueryWithFilter(searchRequest);
        //添加查询条件
        queryBuilder.withQuery(query);
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));

        //添加聚合
        String categoryAggName="category";//商品分类聚合名称
        String brandAggName="brand";//商品品牌聚合名称
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //分类
        List<Category> categories=new ArrayList<>();
        //品牌
        List<Brand> brands=new ArrayList<>();
        //要对商品的分类和品牌进行聚合
        AggregatedPage<Goods> goodspage =(AggregatedPage) goodsRepository.search(queryBuilder.build());

        //获取分类聚合结果
        LongTerms categoryTerms =(LongTerms) goodspage.getAggregation(categoryAggName);

        List<LongTerms.Bucket> buckets = categoryTerms.getBuckets();
        List<Long> cids=new ArrayList<>();
        //从分来的聚合分桶中获取所有分类的id
        for (LongTerms.Bucket bucket : buckets) {
            cids.add(bucket.getKeyAsNumber().longValue());//76 100

        }
        //根据分类的id查询分类的名称
        List<String> names = categoryClient.queryNamesByIds(cids);
        for (int i=0 ;i<cids.size();i++){
            Category category = new Category();
            category.setId(cids.get(i));//76
            category.setName(names.get(i));//手机
            categories.add(category);
        }

        //根据聚合名称职品牌聚合的结果
        LongTerms brandTerms =(LongTerms)goodspage.getAggregation(brandAggName);
        List<LongTerms.Bucket> buckets1 = brandTerms.getBuckets();
        List<Long> brandIds=new ArrayList<>();
        for (LongTerms.Bucket b : buckets1) {
            brandIds.add(b.getKeyAsNumber().longValue());//12669 8557

        }
        //把品牌id转对象集合
        for(Long i:brandIds){
            //根据品牌id查询品牌
            Brand brand = brandClient.queryBrandById(i);
            brands.add(brand);
        }
        //只有分类唯一才展示规格参数
        List<Map<String,Object>> specs=null;
        if(categories.size()==1){
            specs=getSpecs(categories.get(0).getId(),query);
        }

        return new SearchResult(goodspage.getTotalElements(),new Long(goodspage.getTotalPages()),goodspage.getContent(),categories,brands,specs);

    }

    //构建基本查询条件
    private QueryBuilder buildBaseicQueryWithFilter(SearchRequest request) {
        //创建基本的bool查询
        BoolQueryBuilder  queryBuilder=QueryBuilders.boolQuery();
         queryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()).operator(Operator.AND));
            //过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
         //整理过滤条件
        Map<String, String> filter = request.getFilter();
        //filter中的值遍历出来然后交给entry管理
        for(Map.Entry<String ,String> entry:filter.entrySet()){
            //获得传过K
            String key = entry.getKey();
            String value = entry.getValue();
            //商品分类和品牌可以直接查询不需要拼接
            if (key!="cid3"&&key!="brandId") {
                  key="sepcs"+key+"keyword";

            }
            //字符串类型进行 term查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key,value));

        }
        //添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;

    }

    /**
     * 对查询到数据进行可搜索规格参数的聚合操作，聚合时要和，搜索条件相关，搜索到什么内容，聚合什么，不要全部聚合
     * @param id
     * @return
     */
    private List<Map<String, Object>> getSpecs(Long id, QueryBuilder query) {

        List<Map<String,Object>> specList = new ArrayList<>();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //先根据查询条件，执行查询
        queryBuilder.withQuery(query);

        //对规格参数进行聚合，聚合要拿到所有的可搜索的规格参数

        List<SpecParam> searchingSpecParams = this.specClient.querySpecParams(null, id, true,null);

        //添加聚合条件,聚合的名称就是可搜索规格参数的名称,聚合的字段就是合成字段
        searchingSpecParams.forEach(specParam -> queryBuilder.addAggregation(
                AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword")));

        AggregatedPage<Goods> page = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        searchingSpecParams.forEach(specParam -> {
            //可搜索规格参数的名称
            String name = specParam.getName();

            //根据聚合名称获取聚合的结果
            StringTerms stringTerms = (StringTerms) page.getAggregation(name);

            List<String> values = new ArrayList<>();
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();

            //把聚合分桶中每个值存入values集合中
            buckets.forEach(bucket -> values.add(bucket.getKeyAsString()));

            Map<String,Object> specMap = new HashMap<>();
            specMap.put("k",name);//k===>CPU品牌

            specMap.put("options",values);//options===》["骁龙","联发科","展讯"]
            specList.add(specMap);
        });
        return specList;
    }
  /*  public PageResult<Goods> search(SearchRequest searchRequest) {
        String key = searchRequest.getKey();//假设是小米
        if(null==key){
            return null;
        }
        //构建查询条件
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //小米电视必须同时出现在all字段里面 拆分成小米+电视
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));
        //过滤字段 查询我想要的字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        //分页
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));

        //查询
        Page<Goods> goodsPage = goodsRepository.search(queryBuilder.build());


        return new PageResult<>(goodsPage.getTotalElements(),new Long(goodsPage.getTotalPages()),goodsPage.getContent());

    }*/
}
