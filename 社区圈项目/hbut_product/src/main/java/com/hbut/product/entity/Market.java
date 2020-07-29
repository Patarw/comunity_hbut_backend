package com.hbut.product.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**商家信息**/
@Data
@Entity
@Table(name = "market")
public class Market implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自动递增
    private Integer id;

    /**商家名字*/
    private String marketName;

    /**商家描述*/
    private String marketDescribe;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;
}
