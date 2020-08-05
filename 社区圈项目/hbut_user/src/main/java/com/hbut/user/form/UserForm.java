package com.hbut.user.form;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserForm implements Serializable {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生年月日
     */
    private Date birthday;
    /**
     * 头像
     */
    private String avatar;
    /**
     * E-Mail
     */
    private String email;

    /**
     * 兴趣
     */
    private String interest;

}
