package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.adapter.DeliveryAdapter;
import cool.zzy.sems.application.adapter.LogisticsAdapter;
import cool.zzy.sems.application.util.DateUtils;
import cool.zzy.sems.context.dto.DeliveryDTO;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.model.Logistics;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 14:46
 * @since 1.0
 */
public class LogisticsFragment extends BaseFragment {
    private AppCompatTextView userInfo;
    private AppCompatTextView postId;
    private AppCompatTextView created;
    private AppCompatTextView locationName;
    private AppCompatTextView deliveryStatus;
    private AppCompatTextView deliveryInfo;
    private AppCompatTextView deliveryLogistics;
    private AppCompatImageView deliveryAvatar;
    private LinearLayout backLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_logistics;
    }

    @Override
    protected void initViews(View rootView) {
        userInfo = rootView.findViewById(R.id.fragment_logistics_user_info);
        postId = rootView.findViewById(R.id.fragment_logistics_post_id);
        created = rootView.findViewById(R.id.fragment_logistics_created);
        locationName = rootView.findViewById(R.id.fragment_logistics_location_name);
        deliveryStatus = rootView.findViewById(R.id.fragment_logistics_status);
        deliveryInfo = rootView.findViewById(R.id.fragment_logistics_info);
        deliveryLogistics = rootView.findViewById(R.id.fragment_logistics);
        deliveryAvatar = rootView.findViewById(R.id.fragment_logistics_avatar);
        backLayout = rootView.findViewById(R.id.fragment_logistics_back);
        recyclerView = rootView.findViewById(R.id.fragment_logistics_recyclerview);
    }

    @Override
    protected void initData() {
        DeliveryLogistics deliveryLogistics = SemsApplication.instance.getDeliveryLogistics();
        if (deliveryLogistics != null) {
            DeliveryAdapter.setDeliveryLogisticsData(deliveryLogistics, getMainActivity(),
                    deliveryStatus, deliveryInfo, this.deliveryLogistics, deliveryAvatar, false);
            DeliveryDTO delivery = deliveryLogistics.getDelivery();
            userInfo.setText("手机号：" + delivery.getPhone());
            postId.setText("快递单号：" + delivery.getPostId());
            created.setText("创建时间：" + DateUtils.formatDateTime(delivery.getCreated(), TimeUnit.MILLISECONDS));
            locationName.setText("运送地址：" + delivery.getLocationName());
            initRecyclerView(deliveryLogistics.getLogisticsList());
        }
        backLayout.setOnClickListener(this);
    }

    private void initRecyclerView(List<Logistics> logisticsList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        LogisticsAdapter adapter = new LogisticsAdapter(logisticsList, getMainActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_logistics_back:
                enterMainFragment();
                break;
            default:
        }
    }
}
