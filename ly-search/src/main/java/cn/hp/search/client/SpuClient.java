package cn.hp.search.client;

import cn.hp.item.api.SpuApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpuClient extends SpuApi {
}
