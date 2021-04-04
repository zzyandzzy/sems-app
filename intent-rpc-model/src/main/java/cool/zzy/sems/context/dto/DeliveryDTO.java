package cool.zzy.sems.context.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 11:17
 * @since 1.0
 */
@Data
public class DeliveryDTO implements Serializable {
    private static final long serialVersionUID = 3593625541433750056L;
    private Integer id;
    private Date created;
    private Date modified;
    private Integer userId;
    private String postId;
    private Integer deliveryCompanyId;
    private String remark;
    private String locationName;
    private String phone;
    private String deliveryName;
    private String avatarUrl;
    private Integer deliveryStatus;
}
