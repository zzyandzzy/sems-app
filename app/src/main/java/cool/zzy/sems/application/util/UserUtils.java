package cool.zzy.sems.application.util;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.LoginActivity;
import cool.zzy.sems.application.activity.UserActivity;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 11:16
 * @since 1.0
 */
public class UserUtils {

    public static void staticLogin(Activity activity, ProgressDialog progressDialog) {
        UserDTO user = SemsApplication.instance.getUser();
        if (user == null) {
            progressDialog.dismiss();
            return;
        }
        if (user.getUser() == null) {
            SemsApplication.instance.removeUser();
            progressDialog.dismiss();
            return;
        }
        UserService userService = SemsApplication.instance.getUserService();
        if (userService == null) {
            progressDialog.dismiss();
            DialogUtils.showConnectErrorDialog(activity);
            return;
        }
        UserDTO userDTO = userService.signIn(user.getUser().getEmail(), user.getUser().getPasswordHash());
        if (userDTO == null) {
            progressDialog.dismiss();
            Toast.makeText(activity, R.string.login_fail, Toast.LENGTH_LONG).show();
            return;
        }
        SemsApplication.instance.putUser(userDTO);
        progressDialog.dismiss();
        if (activity.getClass() != UserActivity.class) {
            activity.startActivity(new Intent(activity, UserActivity.class));
            activity.finish();
        }
    }

    public static void logout(Activity activity) {
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            UserDTO userDTO = SemsApplication.instance.getUser();
            if (userDTO == null) {
                return;
            }
            User user = userDTO.getUser();
            if (user == null) {
                return;
            }
            boolean logoutSuccess = userService.logout(user.getEmail());
            if (logoutSuccess) {
                SemsApplication.instance.removeUser();
                activity.startActivity(new Intent(activity, LoginActivity.class));
                activity.finish();
            }
        }
    }
}
