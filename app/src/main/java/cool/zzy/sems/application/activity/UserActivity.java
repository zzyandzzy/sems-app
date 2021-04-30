package cool.zzy.sems.application.activity;

import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.adapter.UserPagerAdapter;
import cool.zzy.sems.application.fragment.*;
import cool.zzy.sems.context.enums.UserRoleEnum;
import cool.zzy.sems.ui.coordinatortablayout.CoordinatorTabLayout;

import java.util.ArrayList;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 1:33 下午
 * @since 1.0
 */
public class UserActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private CoordinatorTabLayout coordinatorTabLayout;
    private int[] imageArray, colorArray;
    private final String[] titles = {getString(R.string.tab_user), getString(R.string.tab_logistics_personnel)};
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    // fraggments
    public MainFragment mainFragment;
    public SettingFragment settingFragment;
    public BarcodeFragment userBarcodeFragment;
    public BarcodeFragment logisticsPersonnelBarcodeFragment;
    public BarcodeFragment newDeliveryBarcodeFragment;
    public LogisticsFragment logisticsFragment;
    public LogisticsPersonnelFragment logisticsPersonnelFragment;
    public UserManagerFragment userManagerFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void init() {
        fragments = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.viewpager);
    }

    @Override
    protected void initData() {
        initFragment();
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new UserPagerAdapter(getSupportFragmentManager(), fragments, titles));
        imageArray = new int[]{
                R.mipmap.bg_android,
                R.mipmap.bg_ios
        };
        colorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light
        };

        coordinatorTabLayout = findViewById(R.id.coordinatortablayout);
        coordinatorTabLayout.setTranslucentStatusBar(this)
                .setTitle("Demo")
                .setBackEnable(true)
                .setImageArray(imageArray, colorArray)
                .setupWithViewPager(viewPager);
    }

    private void initFragment() {
        mainFragment = new MainFragment();
        settingFragment = new SettingFragment();
        userBarcodeFragment = new BarcodeFragment(BarcodeFragment.USER_SCAN_TYPE);
        logisticsFragment = new LogisticsFragment();
        if (userRole == null) {
//            setCurrentFragment(mainFragment);
            return;
        }
        UserRoleEnum roleEnum = UserRoleEnum.from(userRole.getRoleName());
        if (roleEnum == UserRoleEnum.USER) {
//            setCurrentFragment(mainFragment);
            return;
        }
        logisticsPersonnelBarcodeFragment = new BarcodeFragment(BarcodeFragment.LOGISTICS_PERSONNEL_SCAN_TYPE);
        newDeliveryBarcodeFragment = new BarcodeFragment(BarcodeFragment.NEW_DELIVERY_SCAN_TYPE);
        logisticsPersonnelFragment = new LogisticsPersonnelFragment();
        if (roleEnum == UserRoleEnum.LOGISTICS_PERSONNEL) {
        } else if (roleEnum == UserRoleEnum.ADMIN) {
            userManagerFragment = new UserManagerFragment();
        }
        fragments.add(mainFragment);
        fragments.add(settingFragment);
    }

    @Override
    protected int getFragmentViewId() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START);
                    return true;
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
                break;
            default:
        }
        return true;
    }
}
