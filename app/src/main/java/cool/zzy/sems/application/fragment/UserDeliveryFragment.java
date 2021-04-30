package cool.zzy.sems.application.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.UserLogisticsActivity;
import cool.zzy.sems.application.adapter.DeliveryAdapter;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.service.DeliveryLogisticsService;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 3:02 下午
 * @since 1.0
 */
public class UserDeliveryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DeliveryAdapter adapter;
    private List<DeliveryLogistics> deliveryLogisticsList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_delivery;
    }

    @Override
    protected void initViews(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefreshlayout);
        recyclerView = rootView.findViewById(R.id.recyclerview);
    }

    @Override
    protected void initData() {
        initRecyclerView();
    }

    @Override
    protected void viewOnClick(View v) {
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeliveryAdapter(deliveryLogisticsList, getActivity());
        adapter.setOnItemClickListener((itemView, position) -> {
            SemsApplication.instance.setDeliveryLogistics(deliveryLogisticsList.get(position));
            getActivity().startActivity(new Intent(getActivity(), UserLogisticsActivity.class));
        });
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    private void recyclerViewRefresh() {
        DeliveryLogisticsService deliveryLogisticsService = SemsApplication.instance.getDeliveryLogisticsService();
        if (deliveryLogisticsService != null) {
            deliveryLogisticsList = deliveryLogisticsService.getListByUid(user.getId());
            adapter.setData(deliveryLogisticsList);
            adapter.notifyDataSetChanged();
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