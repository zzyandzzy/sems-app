package cool.zzy.sems.application.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.MainActivity;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.model.UserRole;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 10:55
 * @since 1.0
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected UserDTO userDTO;
    protected User user;
    protected UserRole userRole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);
        userDTO = SemsApplication.instance.getUser();
        if (userDTO != null) {
            this.user = userDTO.getUser();
            this.userRole = userDTO.getUserRole();
        }
        initViews(rootView);
        initData();
        return rootView;
    }

    protected abstract int getLayout();

    protected abstract void initViews(View rootView);

    protected abstract void initData();

    @Override
    public void onClick(View v) {
        viewOnClick(v);
    }

    protected abstract void viewOnClick(View v);

    @Deprecated
    public void enterSettingFragment() {
        if (getMainActivity().settingFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().settingFragment);
        }
    }

    @Deprecated
    public void enterUserBarcodeFragment() {
        if (getMainActivity().userBarcodeFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().userBarcodeFragment);
        }
    }

    @Deprecated
    public void enterLogisticsPersonnelFragment() {
        if (getMainActivity().logisticsPersonnelFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().logisticsPersonnelFragment);
        }
    }

    @Deprecated
    public void enterNewDeliveryBarcodeFragment() {
        if (getMainActivity().newDeliveryBarcodeFragment != null) {
            getMainActivity().setCurrentFragment(getMainActivity().newDeliveryBarcodeFragment);
        }
    }

    @Deprecated
    public void enterLogisticsPersonnelBarcodeFragment() {
        if (getMainActivity().logisticsPersonnelBarcodeFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().logisticsPersonnelBarcodeFragment);
        }
    }

    @Deprecated
    public void enterMainFragment() {
        if (getMainActivity().mainFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().mainFragment);
        }
    }

    @Deprecated
    public void enterLogisticsFragment() {
        if (getMainActivity().logisticsFragment != null) {
            getMainActivity()
                    .setCurrentFragment(getMainActivity().logisticsFragment);
        }
    }

    @Deprecated
    public MainActivity getMainActivity() {
        return (MainActivity) this.getActivity();
    }
}
