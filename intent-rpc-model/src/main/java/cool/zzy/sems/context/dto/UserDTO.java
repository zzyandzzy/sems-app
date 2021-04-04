package cool.zzy.sems.context.dto;

import cool.zzy.sems.context.model.LogisticsLocation;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 10:35
 * @since 1.0
 */
@AllArgsConstructor
@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -8008510020128196969L;
    private User user;
    private UserRole userRole;
    private LogisticsLocation logisticsLocation;
}
