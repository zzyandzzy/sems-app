package cool.zzy.sems.context.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/1 21:18
 * @since 1.0
 */
@Data
public class DeliveryLogistics implements Serializable {
    private static final long serialVersionUID = 3089742177400240209L;
    private Delivery delivery;
    private DeliveryCompany deliveryCompany;
    private List<Logistics> logisticsList;
}
