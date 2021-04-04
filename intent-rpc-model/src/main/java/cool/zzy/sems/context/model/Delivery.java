package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 快递信息表
 *
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 12:45
 * @since 1.0
 */
@Data
public class Delivery implements Serializable {
    private Integer id;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date modified;

    private Integer userId;

    /**
     * 快递单号
     */
    private String postId;

    /**
     * 快递公司
     */
    private Integer deliveryCompanyId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 快递地址
     */
    private String locationName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 快递名称
     */
    private String deliveryName;

    /**
     * 快递头像
     */
    private String avatarUrl;

    /**
     * 快递状态：1为等待系统确认，2为出库中，3为采购中，
     * 4为运输中，5为派送中，6为已签收，7为已完成，8其他
     */
    private Short deliveryStatus;

    private Short deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_FK_USER_ID = "fk_user_id";

    public static final String COL_POST_ID = "post_id";

    public static final String COL_FK_DELIVERY_COMPANY_ID = "fk_delivery_company_id";

    public static final String COL_REMARK = "remark";

    public static final String COL_LOCATION_NAME = "location_name";

    public static final String COL_PHONE = "phone";

    public static final String COL_DELIVERY_NAME = "delivery_name";

    public static final String COL_AVATAR_URL = "avatar_url";

    public static final String COL_DELIVERY_STATUS = "delivery_status";

    public static final String COL_IS_DELETED = "is_deleted";
}