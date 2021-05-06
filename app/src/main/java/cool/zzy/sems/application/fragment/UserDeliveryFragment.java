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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.activity.BarcodeActivity;
import cool.zzy.sems.application.activity.UserLogisticsActivity;
import cool.zzy.sems.application.adapter.DeliveryAdapter;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.context.enums.DeliveryStatusEnum;
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
    private FloatingActionButton fab;
    private LinearLayout empty;
    private AppCompatSpinner deliveryStatusSpinner;
    private Integer deliveryStatus = DeliveryStatusEnum.WAITING_SYSTEM_CONFIRMATION.getStatus();

    @Override
    protected int getLayout() {
        return R.layout.fragment_user_delivery;
    }

    @Override
    protected void initViews(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.swiperefreshlayout);
        recyclerView = rootView.findViewById(R.id.recyclerview);
        fab = rootView.findViewById(R.id.fab);
        empty = rootView.findViewById(R.id.empty);
        deliveryStatusSpinner = rootView.findViewById(R.id.delivery_status_spinner);
    }

    @Override
    protected void initData() {
        initRecyclerView();
        initDeliverySpinner();
        fab.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this.getActivity(), BarcodeActivity.class);
                intent.putExtra(BarcodeActivity.TYPE_NAME, BarcodeActivity.USER_SCAN_TYPE);
                startActivity(intent);
                break;
            default:
        }
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
            deliveryLogisticsList = deliveryLogisticsService.getDeliveryLogisticsMap(deliveryStatus, user.getId());
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
            DialogUtils.showConnectErrorDialog(this.getActivity());
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
