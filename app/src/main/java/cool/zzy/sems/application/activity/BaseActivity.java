package cool.zzy.sems.application.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.util.IPUtils;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.model.UserRole;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 12:43
 * @since 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Fragment currentFragment;
    protected UserDTO userDTO;
    protected User user;
    protected UserRole userRole;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        userDTO = SemsApplication.instance.getUser();
        if (userDTO != null) {
            this.user = userDTO.getUser();
            this.userRole = userDTO.getUserRole();
        }
        init();
        initViews();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SemsApplication.instance.getIp() == null || "".equals(SemsApplication.instance.getIp())) {
            IPUtils.getMyIp();
        }
    }

    protected abstract int getContentView();

    protected abstract void init();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract int getFragmentViewId();

    public void setCurrentFragment(Fragment fragment) {
        if (fragment == currentFragment) {
            return;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }
        currentFragment = fragment;
        fragmentTransaction.add(getFragmentViewId(), currentFragment).commit();
    }
}
