package cool.zzy.sems.context.service;

import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;

import java.util.List;

/**
 * 用户操作相关接口
 *
 * @author intent zzy.main@gmail.com
 * @date 2021/1/29 15:20
 * @since 1.0
 */
public interface UserService {
    /**
     * 登录
     * 根据用户的邮箱和Hash后的密码在数据库中查询用户
     *
     * @param email    邮箱（唯一）
     * @param password 密码（未Hash之前的原始密码）
     * @return {@link User} or null
     */
    UserDTO signIn(String email, String password);

    /**
     * 注册用户
     *
     * @param user {@link User}
     * @return {@link User}
     * @throws Exception 用户注册异常
     */
    UserDTO register(User user) throws Exception;

    /**
     * 更新用户信息
     *
     * @param user 用户
     * @return {@link User}
     */
    UserDTO updateUser(User user);

    /**
     * 退出用户
     *
     * @param email 用户邮箱
     * @return true
     */
    boolean logout(String email);

    List<User> list();

    boolean removeUserById(int id);
}
