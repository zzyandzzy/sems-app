package cool.zzy.sems.application.activity;

import android.view.Gravity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.adapter.UserPagerAdapter;
import cool.zzy.sems.application.fragment.UserDeliveryFragment;
import cool.zzy.sems.ui.coordinatortablayout.CoordinatorTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 1:33 下午
 * @since 1.0
 */
public class UserActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private CoordinatorTabLayout coordinatorTabLayout;
    private int[] imageArray, colorArray;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    // fragments
    public UserDeliveryFragment userDeliveryFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void init() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.viewpager);
    }

    @Override
    protected void initData() {
        initFragment();
        titleList.add(getString(R.string.delivery_info));
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(new UserPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
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
        userDeliveryFragment = new UserDeliveryFragment();
        fragmentList.add(userDeliveryFragment);
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
