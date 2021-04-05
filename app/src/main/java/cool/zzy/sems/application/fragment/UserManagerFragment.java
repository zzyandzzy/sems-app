package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.ui.UpdateUserInfoDialog;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.SpinnerAdapterUtils;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/5 16:34
 * @since 1.0
 */
public class UserManagerFragment extends BaseFragment {
    private AppCompatSpinner userSpanner;
    private int selectPosition = 0;
    private AppCompatButton deleteUserButton;
    private AppCompatButton searchUserButton;
    private AppCompatButton updateUserButton;
    private ProgressDialog progressDialog;
    private AppCompatEditText userIdInput;

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_manager;
    }

    @Override
    protected void initViews(View rootView) {
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.deleting));
        userSpanner = rootView.findViewById(R.id.fragment_user_manager_delete_user_spanner);
        deleteUserButton = rootView.findViewById(R.id.fragment_user_manager_delete_user_button);
        userIdInput = rootView.findViewById(R.id.fragment_user_manager_user_id);
        searchUserButton = rootView.findViewById(R.id.fragment_user_manager_search_user);
        updateUserButton = rootView.findViewById(R.id.fragment_user_manager_update_user_button);
    }

    @Override
    protected void initData() {
        deleteUserButton.setOnClickListener(this);
        searchUserButton.setOnClickListener(this);
        updateUserButton.setOnClickListener(this);
        initAllUserData();
    }

    private void initAllUserData() {
        SpinnerAdapterUtils.userList = null;
        SpinnerAdapterUtils.initAllUserData(getActivity(), userSpanner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                userIdInput.setText(String.valueOf(SpinnerAdapterUtils.userList.get(position).getId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_manager_delete_user_button:
                progressDialog.setTitle(getString(R.string.deleting));
                progressDialog.show();
                UserService userService = SemsApplication.instance.getUserService();
                if (userService == null) {
                    DialogUtils.showConnectErrorDialog(getActivity());
                    return;
                }
                User user = SpinnerAdapterUtils.userList.get(selectPosition);
                if (user != null) {
                    new Thread(() -> {
                        boolean b = userService.removeUserById(user.getId());
                        getActivity().runOnUiThread(() -> {
                            progressDialog.dismiss();
                            DialogUtils.showTipDialog(getActivity(), b ? getString(R.string.success) : getString(R.string.fail),
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                        });
                    }).start();
                }
                break;
            case R.id.fragment_user_manager_update_user_button:
                User updateUser = SpinnerAdapterUtils.userList.get(selectPosition);
                UpdateUserInfoDialog updateUserInfoDialog = new UpdateUserInfoDialog(getActivity(), updateUser);
                updateUserInfoDialog.show();
                break;
            case R.id.fragment_user_manager_search_user:
                boolean find = false;
                for (int i = 0; i < SpinnerAdapterUtils.userList.size(); i++) {
                    User u = SpinnerAdapterUtils.userList.get(i);
                    if (u.getId() == Integer.parseInt(userIdInput.getText().toString())) {
                        find = true;
                        selectPosition = i;
                        userSpanner.setSelection(i, true);
                        break;
                    }
                }
                if (!find) {
                    Toast.makeText(getActivity(), R.string.not_find, Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }
}
