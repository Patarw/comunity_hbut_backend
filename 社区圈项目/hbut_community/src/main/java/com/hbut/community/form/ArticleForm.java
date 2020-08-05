package com.hbut.community.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ArticleForm implements Serializable {

    @NotEmpty(message = "帖子标题必填")
    private String title;

    @NotEmpty(message = "帖子内容必填")
    private String content;

    @NotNull(message = "用户id必传")
    private Integer userId; //用户id

    private List<String> imgUrl;

}
