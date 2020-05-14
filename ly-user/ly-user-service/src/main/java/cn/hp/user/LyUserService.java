package cn.hp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Ironhide
 * @create 2020-05-14-15:27
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("cn.hp.user.mapper")
public class LyUserService {
    public static void main(String[] args) {
        SpringApplication.run(LyUserService.class, args);
    }
}
