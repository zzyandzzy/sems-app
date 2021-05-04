package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
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
 * @date 2021/2/25 17:49
 * @since 1.0
 */
@Deprecated
public class SettingFragment extends BaseFragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
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
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews(View rootView) {
        settingBack = rootView.findViewById(R.id.fragment_setting_back);
        logoutButton = rootView.findViewById(R.id.fragment_setting_logout);
        saveButton = rootView.findViewById(R.id.fragment_setting_save);
        emailEdittext = rootView.findViewById(R.id.fragment_setting_email);
        usernameEdittext = rootView.findViewById(R.id.fragment_setting_username);
        allUserSpinner = rootView.findViewById(R.id.fragment_setting_all_user_spinner);
        allUserButton = rootView.findViewById(R.id.fragment_setting_all_user_button);
        progressDialog = new ProgressDialog(Objects.requireNonNull(getActivity()), getString(R.string.logging));
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

    private void initAllUserData() {
        SpinnerAdapterUtils.initAllUserData(getActivity(), allUserSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectUserListPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_back:
                enterMainFragment();
                break;
            case R.id.fragment_setting_logout:
                UserUtils.logout(getActivity());
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
                            DialogUtils.showConnectErrorDialog(this.getMainActivity());
                            return;
                        }
                        new Thread(() -> {
                            UserDTO userDTO = userService.signIn(user.getEmail(),
                                    HashUtils.removeSalt(user.getPasswordHash()));
                            getMainActivity().runOnUiThread(() -> {
                                progressDialog.dismiss();
                                if (user == null) {
                                    LoginFragment.loginFail(getMainActivity(), userDTO);
                                } else {
                                    LoginFragment.loginSuccess(getMainActivity(), userDTO);
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
                Toast.makeText(getActivity(), R.string.modify_user_fail, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_LONG).show();
                SemsApplication.instance.putUser(userDTO);
                enterMainFragment();
            }
        } else {
            DialogUtils.showConnectErrorDialog(getActivity());
        }
    }
}
