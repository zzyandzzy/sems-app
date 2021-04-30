package cool.zzy.sems.application.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.fragment.LoginFragment;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.HashUtils;
import cool.zzy.sems.application.util.SpinnerAdapterUtils;
import cool.zzy.sems.application.util.UserUtils;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

import java.util.Objects;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 8:21 下午
 * @since 1.0
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private LinearLayout settingBack;
    private AppCompatButton logoutButton;
    private AppCompatButton saveButton;
    private AppCompatEditText emailEdittext;
    private AppCompatEditText usernameEdittext;
    private AppCompatSpinner allUserSpinner;
    private AppCompatButton allUserButton;
    private int selectUserListPosition = 0;
    private ProgressDialog progressDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {
        settingBack = findViewById(R.id.fragment_setting_back);
        logoutButton = findViewById(R.id.fragment_setting_logout);
        saveButton = findViewById(R.id.fragment_setting_save);
        emailEdittext = findViewById(R.id.fragment_setting_email);
        usernameEdittext = findViewById(R.id.fragment_setting_username);
        allUserSpinner = findViewById(R.id.fragment_setting_all_user_spinner);
        allUserButton = findViewById(R.id.fragment_setting_all_user_button);
        progressDialog = new ProgressDialog(Objects.requireNonNull(this), getString(R.string.logging));
    }

    @Override
    protected void initData() {
        settingBack.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        allUserButton.setOnClickListener(this);
        emailEdittext.setText(user.getEmail());
        usernameEdittext.setText(user.getNickname());
        initAllUserData();
    }

    @Override
    protected int getFragmentViewId() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_back:
                finish();
                break;
            case R.id.fragment_setting_logout:
                UserUtils.logout(this);
                break;
            case R.id.fragment_setting_save:
                saveUser();
                break;
            case R.id.fragment_setting_all_user_button:
                if (SpinnerAdapterUtils.userList != null) {
                    User user = SpinnerAdapterUtils.userList.get(selectUserListPosition);
                    if (user != null) {
                        progressDialog.show();
                        UserService userService = SemsApplication.instance.getUserService();
                        if (userService == null) {
                            progressDialog.dismiss();
                            DialogUtils.showConnectErrorDialog(this);
                            return;
                        }
                        new Thread(() -> {
                            UserDTO userDTO = userService.signIn(user.getEmail(),
                                    HashUtils.removeSalt(user.getPasswordHash()));
                            this.runOnUiThread(() -> {
                                progressDialog.dismiss();
                                if (user == null) {
                                    LoginFragment.loginFail(this, userDTO);
                                } else {
                                    LoginFragment.loginSuccess(this, userDTO);
                                }
                            });
                        }).start();
                    }
                }
                break;
            default:
        }
    }

    private void saveUser() {
        UserService userService = SemsApplication.instance.getUserService();
        if (userService != null) {
            User updateUser = new User();
            updateUser.setEmail(user.getEmail());
            updateUser.setGender(user.getGender());
            updateUser.setNickname(usernameEdittext.getText().toString());
            UserDTO userDTO = userService.updateUser(updateUser);
            if (userDTO == null) {
                Toast.makeText(this, R.string.modify_user_fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show();
                SemsApplication.instance.putUser(userDTO);
                finish();
            }
        } else {
            DialogUtils.showConnectErrorDialog(this);
        }
    }

    private void initAllUserData() {
        SpinnerAdapterUtils.initAllUserData(this, allUserSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectUserListPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
