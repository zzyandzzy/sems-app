package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 14:18
 * @since 1.0
 */
@Data
public class DeliveryCompany implements Serializable {
    private Integer id;

    private Date created;

    private Date modified;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 快递公司头像
     */
    private String avatarUrl;

    private Short deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_COMPANY_NAME = "company_name";

    public static final String COL_AVATAR_URL = "avatar_url";

    public static final String COL_IS_DELETED = "is_deleted";
}