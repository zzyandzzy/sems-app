package cool.zzy.sems.application.activity;

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
import cool.zzy.sems.application.util.EAN13Utils;
import cool.zzy.sems.context.dto.DeliveryDTO;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.model.Logistics;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/30 3:25 下午
 * @since 1.0
 */
public class UserLogisticsActivity extends BaseActivity implements View.OnClickListener {
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
    private AppCompatImageView postIdImage;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_logistics;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initViews() {
        userInfo = findViewById(R.id.user_info);
        postId = findViewById(R.id.post_id);
        created = findViewById(R.id.created);
        locationName = findViewById(R.id.location_name);
        deliveryStatus = findViewById(R.id.status);
        deliveryInfo = findViewById(R.id.info);
        deliveryLogistics = findViewById(R.id.logistics);
        deliveryAvatar = findViewById(R.id.avatar);
        backLayout = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerview);
        postIdImage = findViewById(R.id.post_id_image);
    }

    @Override
    protected void initData() {
        DeliveryLogistics deliveryLogistics = SemsApplication.instance.getDeliveryLogistics();
        if (deliveryLogistics != null) {
            DeliveryAdapter.setDeliveryLogisticsData(deliveryLogistics, this,
                    deliveryStatus, deliveryInfo, this.deliveryLogistics, deliveryAvatar, false);
            DeliveryDTO delivery = deliveryLogistics.getDelivery();
            userInfo.setText(String.format("uid: %d | 手机号：%s", delivery.getUserId(), delivery.getPhone()));
            postId.setText("快递单号：" + delivery.getPostId());
            postIdImage.setImageBitmap(EAN13Utils.drawEan13Code(delivery.getPostId()));
//            postIdImage.setImageBitmap(Code36Utils.drawCode36Code(delivery.getPostId()));
            created.setText("创建时间：" + DateUtils.formatDateTime(delivery.getCreated(), TimeUnit.MILLISECONDS));
            locationName.setText("运送地址：" + delivery.getLocationName());
            initRecyclerView(deliveryLogistics.getLogisticsList());
        }
        backLayout.setOnClickListener(this);
    }

    @Override
    protected int getFragmentViewId() {
        return 0;
    }

    private void initRecyclerView(List<Logistics> logisticsList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LogisticsAdapter adapter = new LogisticsAdapter(logisticsList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            default:
        }
    }
}
