package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 14:18
 * @since 1.0
 */

/**
 * 物流表
 */
@Data
public class Logistics implements Serializable {
    private Integer id;

    private Date created;

    private Date modified;

    /**
     * 快递信息
     */
    private Integer deliveryId;

    /**
     * 当前位置
     */
    private Integer currentLocationId;

    /**
     * 物流信息
     */
    private String description;

    private Integer userId;

    /**
     * 操作人id
     */
    private Integer ctlUserId;

    private Short deleted;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CREATED = "created";

    public static final String COL_MODIFIED = "modified";

    public static final String COL_FK_DELIVERY_ID = "fk_delivery_id";

    public static final String COL_CURRENT_LOCATION_ID = "t_current_location_id";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_FK_USER_ID = "fk_user_id";

    public static final String COL_CTL_USER_ID = "ctl_user_id";

    public static final String COL_IS_DELETED = "is_deleted";
}