package cool.zzy.sems.context.enums;

import lombok.Getter;

/**
 * 快递状态：1为等待系统确认，2为出库中，3为采购中，
 * 4为运输中，5为派送中，6为已签收，7为已完成
 *
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 11:35
 * @since 1.0
 */
@Getter
public enum DeliveryStatusEnum {
    /**
     * 1为等待系统确认
     */
    WAITING_SYSTEM_CONFIRMATION(1, "等待系统确认"),
    IN_THE_OUTBOUND(2, "出库中"),
    PURCHASING(3, "采购中"),
    IN_TRANSIT(4, "运输中"),
    IN_DELIVERY(5, "派送中"),
    SIGNED_IN(6, "已签收"),
    COMPLETED(7, "已完成"),
    OTHER(8, "其他"),
    ;

    private int status;
    private String description;

    DeliveryStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public static DeliveryStatusEnum from(Integer deliveryStatus) {
        if (deliveryStatus == null || deliveryStatus <= 0) {
            return WAITING_SYSTEM_CONFIRMATION;
        }
        for (DeliveryStatusEnum value : values()) {
            if (value.status == deliveryStatus) {
                return value;
            }
        }
        return OTHER;
    }
}
