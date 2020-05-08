import cn.hp.item.pojo.SpuBo;
import cn.hp.search.SearchApplication;
import cn.hp.search.client.SpuClient;
import cn.hp.search.pojo.Goods;
import cn.hp.search.repository.GoodsRepository;
import cn.hp.search.service.IndexService;
import cn.hp.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexCreateTest {

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void loadData() {
        int page = 1;
        while (true) {
            //分页查询所有的spuBo
            PageResult<SpuBo> spuBoPageResult = spuClient.querySpuByPage(null, null, page, 50);
            //如果没查到说明查完了，直接停
            if (null == spuBoPageResult) {
                break;
            }
            page++;
            //从查询结果中获取到所有的spuBo信息
            List<SpuBo> spuBos = spuBoPageResult.getItems();
            List<Goods> goodsList = new ArrayList<>();
            spuBos.forEach(spuBo -> {
                //把spuBo转换为goods
                Goods goods = indexService.buildGoods(spuBo);
                goodsList.add(goods);
            });
            //批量保存goods到索引库
            goodsRepository.saveAll(goodsList);
        }

    }
}