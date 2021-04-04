package cool.zzy.sems.application.activity;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.fragment.BarcodeFragment;
import cool.zzy.sems.application.fragment.LogisticsFragment;
import cool.zzy.sems.application.fragment.MainFragment;
import cool.zzy.sems.application.fragment.SettingFragment;
import cool.zzy.sems.application.util.UserUtils;
import cool.zzy.sems.context.enums.UserRoleEnum;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    public MainFragment mainFragment;
    public SettingFragment settingFragment;
    public BarcodeFragment barcodeFragment;
    public LogisticsFragment logisticsFragment;

    private BottomBar bottomBar;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void initViews() {
        bottomBar = findViewById(R.id.main_bottom_bar);
    }

    @Override
    protected void initData() {
        mainFragment = new MainFragment();
        settingFragment = new SettingFragment();
        barcodeFragment = new BarcodeFragment();
        logisticsFragment = new LogisticsFragment();
        if (userRole == null) {
            bottomBar.setVisibility(View.GONE);
            setCurrentFragment(mainFragment);
            return;
        }
        UserRoleEnum roleEnum = UserRoleEnum.from(userRole.getRoleName());
        if (roleEnum == UserRoleEnum.USER) {
            bottomBar.setVisibility(View.GONE);
            setCurrentFragment(mainFragment);
            return;
        }
        if (roleEnum == UserRoleEnum.LOGISTICS_PERSONNEL) {
            bottomBar.setItems(R.xml.bottombar_tabs_logistics_personnel);
        } else if (roleEnum == UserRoleEnum.ADMIN) {
            bottomBar.setItems(R.xml.bottombar_tabs_admin);
        }
        for (int i = 0; i < bottomBar.getTabCount(); i++) {
            BottomBarTab tab = bottomBar.getTabAtPosition(i);
            tab.setGravity(Gravity.CENTER);
        }
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_user:
                    setCurrentFragment(mainFragment);
                    break;
                default:
            }
        });
    }

    @Override
    protected int getFragmentViewId() {
        return R.id.main_content;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login_out:
                UserUtils.logout(this);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeBottomTab(int pos) {
        bottomBar.selectTabAtPosition(pos, true);
    }
}