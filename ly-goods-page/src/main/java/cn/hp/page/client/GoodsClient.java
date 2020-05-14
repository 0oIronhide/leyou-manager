package cn.hp.page.client;

import cn.hp.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GoodsClient extends SpuApi {
}
