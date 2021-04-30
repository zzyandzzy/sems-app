package cool.zzy.sems.ui.coordinatortablayout.listener;


import com.google.android.material.tabs.TabLayout;

public interface OnTabSelectedListener {

    public void onTabSelected(TabLayout.Tab tab);

    public void onTabUnselected(TabLayout.Tab tab);

    public void onTabReselected(TabLayout.Tab tab);
}
