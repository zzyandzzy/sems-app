package cool.zzy.sems.application.util;

import android.app.Activity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.AppCompatSpinner;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.model.LogisticsLocation;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.DeliveryService;
import cool.zzy.sems.context.service.LogisticsLocationService;
import cool.zzy.sems.context.service.UserService;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/5 11:39
 * @since 1.0
 */
public class SpinnerAdapterUtils {
    public static List<User> userList;
    public static String[] userNameArray;

    public static List<LogisticsLocation> logisticsLocationList;
    public static String[] logisticsLocationNameArray;

    public static String[] deliveryCompanyArray;

    public static List<Delivery> deliveryList;
    public static String[] deliveryNameArray;

    public static void initAllDeliveryCompany(Activity activity, AppCompatSpinner spinner,
                                              AdapterView.OnItemSelectedListener selectedListener) {
        if (deliveryCompanyArray != null) {
            initSpinnerData(activity, spinner, selectedListener, deliveryCompanyArray);
            return;
        }
        deliveryCompanyArray = new String[DeliveryCompanyHandler.DELIVERY_COMPANY_ARRAY.length];
        for (int i = 0; i < DeliveryCompanyHandler.DELIVERY_COMPANY_ARRAY.length; i++) {
            deliveryCompanyArray[i] = DeliveryCompanyHandler.DELIVERY_COMPANY_ARRAY[i].getName();
        }
        initSpinnerData(activity, spinner, selectedListener, deliveryCompanyArray);
    }


    public static void initAllUserData(Activity activity, AppCompatSpinner spinner,
                                       AdapterView.OnItemSelectedListener selectedListener) {
        if (userList != null && userNameArray != null) {
            initSpinnerData(activity, spinner, selectedListener, userNameArray);
            return;
        }
        UserService userService = SemsApplication.instance.getUserService();
        if (userService == null) {
            DialogUtils.showConnectErrorDialog(activity);
            return;
        }
        new Thread(() -> {
            userList = userService.list();
            if (userList != null && !userList.isEmpty()) {
                userNameArray = new String[userList.size()];
                for (int i = 0; i < userList.size(); i++) {
                    userNameArray[i] = userList.get(i).getNickname();
                }
                initSpinnerData(activity, spinner, selectedListener, userNameArray);
            }
        }).start();
    }

    public static void initAllLocationData(Activity activity, AppCompatSpinner spinner,
                                           AdapterView.OnItemSelectedListener selectedListener) {
        if (logisticsLocationList != null && logisticsLocationNameArray != null) {
            initSpinnerData(activity, spinner, selectedListener, logisticsLocationNameArray);
            return;
        }
        LogisticsLocationService logisticsLocationService = SemsApplication.instance.getLogisticsLocationService();
        if (logisticsLocationService == null) {
            DialogUtils.showConnectErrorDialog(activity);
            return;
        }
        new Thread(() -> {
            logisticsLocationList = logisticsLocationService.list();
            activity.runOnUiThread(() -> {
                if (logisticsLocationList != null && !logisticsLocationList.isEmpty()) {
                    logisticsLocationNameArray = new String[logisticsLocationList.size()];
                    for (int i = 0; i < logisticsLocationList.size(); i++) {
                        logisticsLocationNameArray[i] = logisticsLocationList.get(i).getLocationName();
                    }
                    initSpinnerData(activity, spinner, selectedListener, logisticsLocationNameArray);
                }
            });
        }).start();
    }


    public static void initAllDeliveryData(Activity activity, AppCompatSpinner spinner,
                                           AdapterView.OnItemSelectedListener selectedListener) {
        if (deliveryList != null && deliveryNameArray != null) {
            initSpinnerData(activity, spinner, selectedListener, deliveryNameArray);
            return;
        }
        DeliveryService deliveryService = SemsApplication.instance.getDeliveryService();
        if (deliveryService == null) {
            DialogUtils.showConnectErrorDialog(activity);
            return;
        }
        new Thread(() -> {
            deliveryList = deliveryService.list();
            activity.runOnUiThread(() -> {
                if (deliveryList != null && !deliveryList.isEmpty()) {
                    deliveryNameArray = new String[deliveryList.size()];
                    for (int i = 0; i < deliveryList.size(); i++) {
                        deliveryNameArray[i] = deliveryList.get(i).getDeliveryName();
                    }
                    initSpinnerData(activity, spinner, selectedListener, deliveryNameArray);
                }
            });
        }).start();
    }

    private static void initSpinnerData(Activity activity, AppCompatSpinner spinner,
                                        AdapterView.OnItemSelectedListener selectedListener,
                                        String[] array) {
        activity.runOnUiThread(() -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    activity, android.R.layout.simple_list_item_1, array);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(selectedListener);
        });
    }
}
