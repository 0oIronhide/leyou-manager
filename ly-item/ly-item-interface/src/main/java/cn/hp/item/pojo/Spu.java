package cn.hp.item.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "tb_spu")
public class Spu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String subTitle;

    private Long cid1;

    private Long cid2;

    private Long cid3;

    private Long brandId;

    private Boolean saleable;

    private Boolean valid;

    private Timestamp createTime;

    private Timestamp lastUpdateTime;
}