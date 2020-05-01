package cn.hp.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;

    private String genericSpec;

    private String specialSpec;

    private String packingList;

    private String afterService;

    private String description;

}