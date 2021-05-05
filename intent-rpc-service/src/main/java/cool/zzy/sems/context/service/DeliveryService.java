package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.Delivery;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 13:42
 * @since 1.0
 */
public interface DeliveryService {
    /**
     * 添加一条物流信息
     *
     * @param delivery 物流信息
     * @return true add success
     */
    boolean saveDelivery(Delivery delivery);

    List<Delivery> list();

    boolean removeDeliveryById(int id);
}

