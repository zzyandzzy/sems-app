package cool.zzy.sems.context.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 20:42
 * @since 1.0
 */
@AllArgsConstructor
@Data
public class LogisticsAddDTO implements Serializable {
    private static final long serialVersionUID = 2187015159229036944L;

    public static final LogisticsAddDTO DELIVERY_EMPTY = new LogisticsAddDTO(-1, "没有该快递");
    public static final LogisticsAddDTO ARGS_ERROR = new LogisticsAddDTO(-2, "参数错误");
    public static final LogisticsAddDTO ROLE_ERROR = new LogisticsAddDTO(-3, "用户权限错误");
    public static final LogisticsAddDTO LOGISTICS_LOCATION_ERROR = new LogisticsAddDTO(-4, "位置获取错误");
    public static final LogisticsAddDTO FAIL = new LogisticsAddDTO(-5, "失败");
    public static final LogisticsAddDTO DELIVERY_STATUS_ERROR = new LogisticsAddDTO(-6, "物流状态错误");
    public static final LogisticsAddDTO SUCCESS = new LogisticsAddDTO(1, "成功");

    private Integer state;
    private String info;
}
