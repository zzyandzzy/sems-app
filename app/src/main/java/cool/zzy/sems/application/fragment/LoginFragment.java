package cool.zzy.sems.application.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.LoginActivity;
import cool.zzy.sems.application.activity.UserActivity;
import cool.zzy.sems.application.constant.Const;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.ui.ReConnectServerDialog;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.RegexUtils;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.service.UserService;

import java.util.Objects;

/**
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:13
 * @since 1.0
 */
public class LoginFragment extends BaseFragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    private AppCompatEditText emailEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatButton loginButton;
    private AppCompatButton registerButton;
    private AppCompatCheckBox clauseCheckBox;
    private AppCompatTextView clauseText;
    private AppCompatTextView reConnectServer;

    private ProgressDialog progressDialog;

    private boolean isLogin = false;

    private void loginCancel() {
        progressDialog.dismiss();
        isLogin = false;
    }

    /**
     * 登录
     */
    private void login() {
        // 登录状态设置为true
        isLogin = true;
        // 显示登录进度框
        progressDialog.show();
        // 获取到UserService实例
        UserService userService = SemsApplication.instance.getUserService();
        // 如果UserService不为空
        if (userService != null) {
            // 新开一个线程
            new Thread(() -> {
                // 通过RPC远程调用后端的方法，登录
                UserDTO user = userService.signIn(Objects.requireNonNull(emailEditText.getText()).toString(),
                        Objects.requireNonNull(passwordEditText.getText()).toString());
                // 在主线程执行注册成功的UI展示（只有主线程能够进行UI的更新）
                getLoginActivity().runOnUiThread(() -> {
                    loginCancel();
                    if (user == null) {
                        // 登录失败
                        loginFail(getLoginActivity(), user);
                    } else {
                        // 登录成功
                        loginSuccess(getLoginActivity(), user);
                    }
                });
            }).start();
        } else {
            loginCancel();
            DialogUtils.showConnectErrorDialog(this.getLoginActivity());
        }
    }

    public static void loginFail(Activity activity, UserDTO user) {
        SemsApplication.instance.removeUser();
        Toast.makeText(activity, R.string.login_fail, Toast.LENGTH_LONG).show();
    }

    public static void loginSuccess(Activity activity, UserDTO user) {
        SemsApplication.instance.putUser(user);
        Toast.makeText(activity, String.format("用户: %s 登录成功!", user.getUser().getEmail()),
                Toast.LENGTH_LONG).show();
        activity.startActivity(new Intent(activity, UserActivity.class));
        activity.finish();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews(View rootView) {
        progressDialog = new ProgressDialog(Objects.requireNonNull(getLoginActivity()), getString(R.string.logging));
        emailEditText = rootView.findViewById(R.id.fragment_login_account);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register);
        clauseCheckBox = rootView.findViewById(R.id.fragment_login_clause);
        clauseText = rootView.findViewById(R.id.fragment_login_clause_text);
        reConnectServer = rootView.findViewById(R.id.re_connect_server);
    }

    @Override
    protected void initData() {
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        reConnectServer.setOnClickListener(this);
    }

    private LoginActivity getLoginActivity() {
        return (LoginActivity) this.getActivity();
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_login_button:
                if (checkLoginArgs() && !isLogin) {
                    login();
                }
                break;
            case R.id.fragment_login_register:
                LoginActivity loginActivity = getLoginActivity();
                loginActivity.setCurrentFragment(loginActivity.registerFragment);
                break;
            case R.id.re_connect_server:
                ReConnectServerDialog reConnectServerDialog = new ReConnectServerDialog(getActivity());
                reConnectServerDialog.show();
                break;
            default:
        }
    }

    /**
     * 检查登录参数是否正确
     *
     * @return
     */
    private boolean checkLoginArgs() {
        if (!clauseCheckBox.isChecked()) {
            Toast.makeText(getLoginActivity(), R.string.clause_error, Toast.LENGTH_LONG).show();
            return false;
        }
        if (emailEditText.getText() == null || "".equals(emailEditText.getText().toString())) {
            emailEditText.setError(getString(R.string.email_cannot_empty));
            return false;
        } else if (!RegexUtils.isEmail(emailEditText.getText())) {
            emailEditText.setError(getString(R.string.email_format_error));
            return false;
        }
        if (passwordEditText.getText() == null || "".equals(passwordEditText.getText().toString())) {
            passwordEditText.setError(getString(R.string.password_cannot_empty));
            return false;
        } else if (passwordEditText.getText().toString().length() < Const.PASSWORD_MIN_LEN) {
            passwordEditText.setError(getString(R.string.password_length_error));
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearViewsData();
    }

    private void clearViewsData() {
        try {
            Objects.requireNonNull(emailEditText.getText()).clear();
            Objects.requireNonNull(passwordEditText.getText()).clear();
            clauseCheckBox.setChecked(false);
        } catch (Exception ignored) {
        }
    }
}
