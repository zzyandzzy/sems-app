package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 10:57
 * @since 1.0
 */
@Data
public class UserRole implements Serializable {
    private Integer id;

    private Date created;

    private Date modified;

    private Integer userId;

    private Short deleted;

    private String roleName;

    /**
     * 权限范围
     */
    private Integer roleLocation;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_FK_USER_ID = "fk_user_id";

    public static final String COL_IS_DELETED = "is_deleted";

    public static final String COL_ROLE_NAME = "role_name";

    public static final String COL_ROLE_LOCATION = "role_location";
}