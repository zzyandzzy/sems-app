package cool.zzy.sems.application.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.ui.ProgressDialog;
import cool.zzy.sems.application.util.DeliveryCompanyHandler;
import cool.zzy.sems.application.util.DialogUtils;
import cool.zzy.sems.application.util.EAN13Utils;
import cool.zzy.sems.application.util.SpinnerAdapterUtils;
import cool.zzy.sems.context.enums.UserRoleEnum;
import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.model.LogisticsLocation;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.DeliveryService;

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
    private AppCompatSpinner adminAllUserSpinner;
    private int selectUserListPosition = 0;
    private AppCompatSpinner adminAllDeliveryCompanySpinner;
    private int selectDeliveryCompanyPosition = 0;
    private AppCompatEditText newDeliveryLocationEditText;
    private AppCompatEditText newDeliveryNameEditText;
    private AppCompatEditText newDeliveryPhoneEditText;
    private AppCompatEditText adminDeliveryPostId;
    private AppCompatButton adminDeliveryPostIdRandomButton;
    private AppCompatButton adminDeliverySave;
    private ProgressDialog progressDialog;
    private CardView adminDeleteDelivery;
    private AppCompatSpinner adminAllDeliverySpinner;
    private AppCompatButton adminDeliveryDelete;
    private int selectDeliveryPosition = 0;

    @Override
    protected int getLayout() {
        return R.layout.fragment_logistics_personnel;
    }

    @Override
    protected void initViews(View rootView) {
        scanImageView = rootView.findViewById(R.id.fragment_logistics_personnel_scan);
        newDeliveryScanImageView = rootView.findViewById(R.id.fragment_logistics_personnel_post_id_scan);
        locationSpinner = rootView.findViewById(R.id.fragment_logistics_personnel_location_spinner);
        adminAllUserSpinner = rootView.findViewById(R.id.fragment_logistics_personnel_admin_all_user_spinner);
        adminAllDeliveryCompanySpinner = rootView.findViewById(R.id.fragment_logistics_personnel_admin_all_delivery_company_spinner);
        newDeliveryLocationEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_location);
        newDeliveryNameEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_name);
        newDeliveryPhoneEditText = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_phone);
        adminDeliveryPostId = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_post_id);
        adminDeliveryPostIdRandomButton = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_post_id_random);
        adminDeliverySave = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_save);
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.saveing_delivery));
        adminDeleteDelivery = rootView.findViewById(R.id.fragment_logistics_personnel_delete_delivery);
        adminAllDeliverySpinner = rootView.findViewById(R.id.fragment_logistics_personnel_delete_delivery_spinner);
        adminDeliveryDelete = rootView.findViewById(R.id.fragment_logistics_personnel_admin_delivery_delete);
    }

    @Override
    protected void initData() {
        adminDeliveryPostId.setText(EAN13Utils.randomCode());
        adminDeliveryPostIdRandomButton.setOnClickListener(this);
        adminDeliverySave.setOnClickListener(this);
        scanImageView.setOnClickListener(this);
        newDeliveryScanImageView.setOnClickListener(this);
        adminDeliveryDelete.setOnClickListener(this);
        UserRoleEnum roleEnum = UserRoleEnum.from(userRole.getRoleName());
        if (roleEnum == UserRoleEnum.ADMIN) {
            initAllLocationData();
            initAllDeliveryData();
        } else {
            locationSpinner.setVisibility(View.GONE);
            adminDeleteDelivery.setVisibility(View.GONE);
        }
        initAllUserData();
        intiAllDeliveryCompanyData();
    }

    private void initAllDeliveryData() {
        SpinnerAdapterUtils.deliveryNameArray = null;
        selectDeliveryPosition = 0;
        SpinnerAdapterUtils.initAllDeliveryData(getActivity(), adminAllDeliverySpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDeliveryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                initNewDelivery(true);
                break;
            case R.id.fragment_logistics_personnel_admin_delivery_post_id_random:
                adminDeliveryPostId.setText(EAN13Utils.randomCode());
                break;
            case R.id.fragment_logistics_personnel_admin_delivery_save:
                initNewDelivery(false);
                break;
            case R.id.fragment_logistics_personnel_admin_delivery_delete:
                progressDialog.setTitle(getString(R.string.deleting));
                progressDialog.show();
                Delivery delivery = SpinnerAdapterUtils.deliveryList.get(selectDeliveryPosition);
                if (delivery != null) {
                    DeliveryService deliveryService = SemsApplication.instance.getDeliveryService();
                    if (deliveryService == null) {
                        DialogUtils.showConnectErrorDialog(getActivity());
                        return;
                    }
                    new Thread(() -> {
                        boolean b = deliveryService.removeDeliveryById(delivery.getId());
                        getMainActivity().runOnUiThread(() -> {
                            progressDialog.dismiss();
                            DialogUtils.showTipDialog(getActivity(), b ? getString(R.string.success) : getString(R.string.fail),
                                    (dialog, which) -> {
                                        dialog.dismiss();
                                        if (b) {
                                            initAllDeliveryData();
                                        }
                                    });
                        });
                    }).start();
                }
                break;
            default:
        }
    }

    public void initNewDelivery(boolean enterBarcode) {
        if (newDeliveryNameEditText.getText() == null || "".equals(newDeliveryNameEditText.getText().toString())) {
            Toast.makeText(getActivity(), R.string.input_delivery_name_hint, Toast.LENGTH_LONG).show();
            return;
        }
        if (newDeliveryLocationEditText.getText() == null || "".equals(newDeliveryLocationEditText.getText().toString())) {
            Toast.makeText(getActivity(), R.string.input_delivery_location_hint, Toast.LENGTH_LONG).show();
            return;
        }
        if (newDeliveryPhoneEditText.getText() == null || "".equals(newDeliveryPhoneEditText.getText().toString())) {
            Toast.makeText(getActivity(), R.string.input_phone_hint, Toast.LENGTH_LONG).show();
            return;
        }
        adminDeliveryPostId.setText(EAN13Utils.randomCode());
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
                if (enterBarcode) {
                    enterNewDeliveryBarcodeFragment();
                } else {
                    BarcodeFragment.newDelivery(getMainActivity(), progressDialog, adminDeliveryPostId.getText().toString(), false);
                }
            }
        }
    }
}
