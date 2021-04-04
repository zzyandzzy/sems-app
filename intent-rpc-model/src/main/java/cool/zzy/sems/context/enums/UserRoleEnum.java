package cool.zzy.sems.context.enums;

import lombok.Getter;

/**
 * 用户权限
 *
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 10:30
 * @since 1.0
 */
@Getter
public enum UserRoleEnum {
    // 管理员
    ADMIN("ADMIN", 1),
    // 普通用户
    USER("USER", 0),
    // 物流人员
    LOGISTICS_PERSONNEL("LOGISTICS_PERSONNEL", 0),
    ;

    private String name;
    private int location;

    UserRoleEnum(String name, int location) {
        this.name = name;
        this.location = location;
    }

    public static UserRoleEnum from(String roleName) {
        for (UserRoleEnum roleEnum : values()) {
            if (roleEnum.getName().equals(roleName)) {
                return roleEnum;
            }
        }
        return USER;
    }
}
