package cn.hp.page;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/11/16:17
 * @Description:
 */
@EnableDiscoveryClient //伊鲁卡客户端
@SpringBootApplication//启动
@EnableFeignClients//fegin客户端
public class LyGoodsPage {

    public static void main(String[] args) {
        SpringApplication.run(LyGoodsPage.class,args);
    }
}
