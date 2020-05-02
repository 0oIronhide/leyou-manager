package cn.hp.item.pojo;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Data
public class SpuBo extends Spu {

    private String cname;

    private String bname;

    private SpuDetail spuDetail;

    private List<Sku> skus;

}