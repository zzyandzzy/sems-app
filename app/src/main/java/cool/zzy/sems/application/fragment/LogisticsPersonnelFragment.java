package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.util.DeliveryCompanyHandler;
import cool.zzy.sems.application.util.SpinnerAdapterUtils;
import cool.zzy.sems.context.enums.UserRoleEnum;
import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.model.LogisticsLocation;
import cool.zzy.sems.context.model.User;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/4 21:19
 * @since 1.0
 */
public class LogisticsPersonnelFragment extends BaseFragment {
    private AppCompatImageView scanImageView;
    private AppCompatImageView newDeliveryScanImageView;
    // admin
    private int selectLocationListPosition = 0;
    private AppCompatSpinner locationSpinner;
    private CardView adminDeliveryCardView;
    private AppCompatSpinner adminAllUserSpinner;
    private int selectUserListPosition = 0;
    private AppCompatSpinner adminAllDeliveryCompanySpinner;
    private int selectDeliveryCompanyPosition = 0;
    private AppCompatEditText newDeliveryLocationEditText;
    private AppCompatEditText newDeliveryNameEditText;
    private AppCompatEditText newDeliveryPhoneEditText;

    @Override
    protected int getLayout() {
        return R.layout.fragment_logistics_personnel;
    }

    @Override
    protected void initViews(View rootView) {
        scanImageView = rootView.findViewById(R.id.fragment_logistics_personnel_scan);
        newDeliveryScanImageView = rootView.findViewById(R.id.fragment_logistics_personnel_post_id_scan);
        locationSpinner = rootView.findViewById(R.id.fragment_logistics_personnel_location_spinner);
        adminDeliveryCardView = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery);
        adminAllUserSpinner = rootView.findViewById(R.id.fragment_logistics_personnel_admin_all_user_spinner);
        adminAllDeliveryCompanySpinner = rootView.findViewById(R.id.fragment_logistics_personnel_admin_all_delivery_company_spinner);
        newDeliveryLocationEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_location);
        newDeliveryNameEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_name);
        newDeliveryPhoneEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_phone);
    }

    @Override
    protected void initData() {
        scanImageView.setOnClickListener(this);
        newDeliveryScanImageView.setOnClickListener(this);
        UserRoleEnum roleEnum = UserRoleEnum.from(userRole.getRoleName());
        if (roleEnum == UserRoleEnum.ADMIN) {
            initAllLocationData();
        } else {
            locationSpinner.setVisibility(View.GONE);
        }
        initAllUserData();
        intiAllDeliveryCompanyData();
    }

    private void intiAllDeliveryCompanyData() {
        SpinnerAdapterUtils.initAllDeliveryCompany(getActivity(), adminAllDeliveryCompanySpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDeliveryCompanyPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initAllUserData() {
        SpinnerAdapterUtils.initAllUserData(getActivity(), adminAllUserSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectUserListPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initAllLocationData() {
        SpinnerAdapterUtils.initAllLocationData(getActivity(), locationSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectLocationListPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void viewOnClick(View v) {
        UserRoleEnum roleEnum = UserRoleEnum.from(userRole.getRoleName());
        switch (v.getId()) {
            case R.id.fragment_logistics_personnel_scan:
                if (roleEnum == UserRoleEnum.ADMIN && SpinnerAdapterUtils.logisticsLocationList != null) {
                    LogisticsLocation logisticsLocation = SpinnerAdapterUtils.logisticsLocationList.get(selectLocationListPosition);
                    if (logisticsLocation != null) {
                        SemsApplication.instance.getUser().setLogisticsLocation(logisticsLocation);
                    }
                }
                enterLogisticsPersonnelBarcodeFragment();
                break;
            case R.id.fragment_logistics_personnel_post_id_scan:
                User newDeliveryUser = SpinnerAdapterUtils.userList.get(selectUserListPosition);
                if (newDeliveryUser != null) {
                    DeliveryCompanyHandler.DeliveryCompanyEntity newDeliveryCompany =
                            DeliveryCompanyHandler.DELIVERY_COMPANY_ARRAY[selectDeliveryCompanyPosition];
                    if (newDeliveryCompany != null) {
                        Delivery delivery = new Delivery();
                        delivery.setLocationName(newDeliveryLocationEditText.getText().toString());
                        delivery.setPhone(newDeliveryPhoneEditText.getText().toString());
                        delivery.setDeliveryName(newDeliveryNameEditText.getText().toString());
                        delivery.setUserId(newDeliveryUser.getId());
                        delivery.setDeliveryCompanyId(newDeliveryCompany.getId());
                        delivery.setRemark(newDeliveryCompany.getName());
                        SemsApplication.instance.setNewDelivery(delivery);
                        enterNewDeliveryBarcodeFragment();
                    }
                }
                break;
            default:
        }
    }
}
