package VO;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//与前端进行交互的实体类，因为有些东西不能传到前端
@Data
public class UserVO implements Serializable {

    /**
     * ID
     */
    private Integer id;
    /**
     * 手机号码
     */
    private String mobile;
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
     * 注册日期
     */
    private Date regdate;
    /**
     * 修改日期
     */
    private Date updatedate;
    /**
     * 最后登陆日期
     */
    private Date lastdate;
    /**
     * 兴趣
     */
    private String interest;
    /**
     * 粉丝数
     */
    private Integer fanscount;
    /**
     * 关注数
     */
    private Integer followcount;

}

