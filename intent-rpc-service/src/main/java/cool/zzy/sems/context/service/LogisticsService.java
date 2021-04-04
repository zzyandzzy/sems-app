package cool.zzy.sems.context.service;

import cool.zzy.sems.context.dto.LogisticsAddDTO;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.Logistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/28 14:00
 * @since 1.0
 */
public interface LogisticsService {
    /**
     * 根据uid查询物流信息
     *
     * @param uid uid
     * @return list
     */
    List<Logistics> getListByUserId(Integer uid);


    /**
     * 增加一条物流信息
     *
     * @param postId 快递单号
     * @param ctlUser 操作人
     * @return
     */
    LogisticsAddDTO addLogistics(String postId, UserDTO ctlUser);
}
