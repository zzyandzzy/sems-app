package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.ui.ProgressDialog;
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
    private AppCompatSpinner deleteUserSpanner;
    private AppCompatButton deleteUserButton;
    private int deleteUserPosition = 0;
    private ProgressDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_manager;
    }

    @Override
    protected void initViews(View rootView) {
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.deleting));
        deleteUserSpanner = rootView.findViewById(R.id.fragment_user_manager_delete_user_spanner);
        deleteUserButton = rootView.findViewById(R.id.fragment_user_manager_delete_user_button);
    }

    @Override
    protected void initData() {
        deleteUserButton.setOnClickListener(this);
        initAllUserData();
    }

    private void initAllUserData() {
        SpinnerAdapterUtils.userList = null;
        SpinnerAdapterUtils.initAllUserData(getActivity(), deleteUserSpanner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deleteUserPosition = position;
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
                User user = SpinnerAdapterUtils.userList.get(deleteUserPosition);
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
            default:
        }
    }
}
