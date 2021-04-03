package cool.zzy.sems.application.fragment;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.adapter.DeliveryAdapter;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.UserUtils;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.service.DeliveryLogisticsService;

import java.util.List;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/7 10:54
 * @since 1.0
 */
public class MainFragment extends BaseFragment {
    private AppCompatTextView nicknameTextView;
    private AppCompatEditText inputEditText;
    private AppCompatImageView scanImageView;
    private AppCompatButton settingButton;
    private RecyclerView recyclerView;
    private List<DeliveryLogistics> deliveryLogisticsList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews(View rootView) {
        nicknameTextView = rootView.findViewById(R.id.fragment_main_username);
        inputEditText = rootView.findViewById(R.id.fragment_main_input);
        scanImageView = rootView.findViewById(R.id.fragment_main_scan);
        settingButton = rootView.findViewById(R.id.fragment_main_setting);
        recyclerView = rootView.findViewById(R.id.fragment_main_recyclerview);
    }

    @Override
    protected void initData() {
        if (user != null) {
            nicknameTextView.setText(user.getNickname());
        } else {
            UserUtils.staticLogin(getActivity());
        }
        scanImageView.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        DeliveryLogisticsService deliveryLogisticsService = SemsApplication.instance.getDeliveryLogisticsService();
        if (deliveryLogisticsService != null) {
            deliveryLogisticsList = deliveryLogisticsService.getListByUid(user.getId());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.setLayoutManager(layoutManager);
            DeliveryAdapter adapter = new DeliveryAdapter(deliveryLogisticsList, getActivity());
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener((itemView, position) -> {
                SemsApplication.instance.setDeliveryLogistics(deliveryLogisticsList.get(position));
                enterLogisticsFragment();
            });
        } else {
            DialogUtils.showConnectErrorDialog(this.getMainActivity());
        }
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_setting:
                enterSettingFragment();
                break;
            case R.id.fragment_main_scan:
                enterBarcodeFragment();
                break;
            default:
        }
    }
}
