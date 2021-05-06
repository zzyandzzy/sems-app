package cool.zzy.sems.application.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.UserLogisticsActivity;
import cool.zzy.sems.application.adapter.DeliveryAdapter;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.SpinnerAdapterUtils;
import cool.zzy.sems.context.enums.DeliveryStatusEnum;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.DeliveryLogisticsService;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 9:22 下午
 * @since 1.0
 */
public class DeliveryListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = DeliveryListFragment.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DeliveryAdapter adapter;
    private List<DeliveryLogistics> deliveryLogisticsList;
    private LinearLayout empty;
    private AppCompatSpinner deliveryStatusSpinner;
    private Integer deliveryStatus = 0;
    private AppCompatSpinner allUserSpinner;
    private int selectUserListPosition = -1;
    private List<User> userList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_delivery;
    }

    @Override
    protected void initViews(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefreshlayout);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        empty = rootView.findViewById(R.id.empty);
        deliveryStatusSpinner = rootView.findViewById(R.id.delivery_status_spinner);
        allUserSpinner = rootView.findViewById(R.id.all_user_spinner);
    }

    @Override
    protected void initData() {
        initDeliverySpinner();
        initAllUserData();
        initRecyclerView();
    }

    private void initAllUserData() {
        User allUser = new User();
        allUser.setId(0);
        allUser.setNickname("全部");
        SpinnerAdapterUtils.initAllUserData(getActivity(), allUserSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectUserListPosition = position;
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        }, allUser, list -> {
            userList = list;
        });
    }

    private void initDeliverySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_list_item_1,
                DeliveryStatusEnum.toList());
        deliveryStatusSpinner.setAdapter(adapter);
        deliveryStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deliveryName = (String) parent.getSelectedItem();
                deliveryStatus = DeliveryStatusEnum.from(deliveryName).getStatus();
                onRefresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void viewOnClick(View v) {
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeliveryAdapter(deliveryLogisticsList, getActivity());
        adapter.setOnItemClickListener((itemView, position) -> {
            SemsApplication.instance.setDeliveryLogistics(deliveryLogisticsList.get(position));
            getActivity().startActivity(new Intent(getActivity(), UserLogisticsActivity.class));
        });
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void recyclerViewRefresh() {
        DeliveryLogisticsService deliveryLogisticsService = SemsApplication.instance.getDeliveryLogisticsService();
        if (deliveryLogisticsService != null) {
            if (selectUserListPosition == -1) {
                deliveryLogisticsList = deliveryLogisticsService.getDeliveryLogisticsMap(deliveryStatus, 0);
            } else {
                deliveryLogisticsList = deliveryLogisticsService.getDeliveryLogisticsMap(deliveryStatus,
                        userList.get(selectUserListPosition).getId());
            }
            adapter.setData(deliveryLogisticsList);
            adapter.notifyDataSetChanged();
            if (deliveryLogisticsList.size() != 0) {
                recyclerView.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            }
        } else {
            DialogUtils.showConnectErrorDialog(this.getMainActivity());
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> {
            recyclerViewRefresh();
            swipeRefreshLayout.setRefreshing(false);
        }, 500);
    }
}
