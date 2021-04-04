package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 11:42
 * @since 1.0
 */
@Data
public class User implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date modified;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码HASH
     */
    private String passwordHash;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别，true为男，false为女
     */
    private Boolean gender;

    /**
     * IP地址
     */
    private String ip;

    private Short deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_UK_EMAIL = "uk_email";

    public static final String COL_PASSWORD_HASH = "password_hash";

    public static final String COL_NICKNAME = "nickname";

    public static final String COL_GENDER = "gender";

    public static final String COL_IP = "ip";

    public static final String COL_IS_DELETED = "is_deleted";
}