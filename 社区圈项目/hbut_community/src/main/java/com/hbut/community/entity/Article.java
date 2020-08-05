package com.hbut.community.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "article")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title; //帖子标题
    private String content; //帖子内容
    private Integer userId; //用户id
    private Integer checkStatus; //帖子状态，0待审核、1审核通过、2审核不通过
    private String img; //图片url
    private Integer views; //浏览次数
    private Integer likes; //点赞数

    @Type( type = "json" )
    @Column( columnDefinition = "json" )
    private List<String> imgUrl;

    private Date createTime;
    private Date updateTime;
}
