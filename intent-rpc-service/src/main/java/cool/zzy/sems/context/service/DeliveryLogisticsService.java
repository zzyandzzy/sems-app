package cool.zzy.sems.context.service;

import cool.zzy.sems.context.model.DeliveryLogistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/1 21:17
 * @since 1.0
 */
public interface DeliveryLogisticsService {
    /**
     * 根据uid获取list
     *
     * @param uid uid
     * @return
     */
    List<DeliveryLogistics> getListByUid(Integer uid);
}
