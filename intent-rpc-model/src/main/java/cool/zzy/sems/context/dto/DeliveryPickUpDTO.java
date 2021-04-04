package cool.zzy.sems.context.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 11:17
 * @since 1.0
 */
@AllArgsConstructor
@Data
public class DeliveryPickUpDTO implements Serializable {
    public static final DeliveryPickUpDTO ARGS_ERROR = new DeliveryPickUpDTO(-2, "参数错误");
    public static final DeliveryPickUpDTO DELIVERY_EMPTY = new DeliveryPickUpDTO(-1, "没有该快递");
    public static final DeliveryPickUpDTO DELIVERY_PICK_UP_AGAIN = new DeliveryPickUpDTO(-3, "该快递已取件");
    public static final DeliveryPickUpDTO DELIVERY_PICK_UP_UPDATE_STATUS_ERROR = new DeliveryPickUpDTO(-4, "快递状态更新错误");
    public static final DeliveryPickUpDTO SUCCESS = new DeliveryPickUpDTO(1, "取件成功");

    private static final long serialVersionUID = -8962736012958250051L;
    private Integer state;
    private String info;
}
