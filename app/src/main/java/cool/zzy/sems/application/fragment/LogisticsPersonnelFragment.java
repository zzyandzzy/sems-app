package cool.zzy.sems.application.fragment;

import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import cool.zzy.sems.application.R;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 21:19
 * @since 1.0
 */
public class LogisticsPersonnelFragment extends BaseFragment {
    private AppCompatButton scanButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_logistics_personnel;
    }

    @Override
    protected void initViews(View rootView) {
        scanButton = rootView.findViewById(R.id.fragment_logistics_personnel_scan);
    }

    @Override
    protected void initData() {
        scanButton.setOnClickListener(this);
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_logistics_personnel_scan:
                enterLogisticsPersonnelBarcodeFragment();
                break;
            default:
        }
    }
}
