package cool.zzy.sems.application.util;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 11:32
 * @since 1.0
 */
public class DialogUtils {
    public static void showTipDialog(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        showDialog(activity,
                activity.getString(R.string.tips),
                message, activity.getString(R.string.ok), null,
                okListener, null);
    }

    public static void showConnectErrorDialog(Activity activity) {
        showConnectErrorDialog(activity, (dialog1, which) -> {
            SemsApplication.instance.initRPC();
        });
    }

    public static void showConnectErrorDialog(Activity activity,
                                              DialogInterface.OnClickListener reconnectListener) {
        showDialog(activity, R.string.error, R.string.connect_rpc_error, R.string.reconnect, R.string.exit,
                reconnectListener, (dialog12, which) -> activity.finish());
    }

    public static void showDialog(Activity activity, int title, int message, int okText, int cancelText,
                                  DialogInterface.OnClickListener okListener,
                                  DialogInterface.OnClickListener cancelListener) {
        showDialog(activity,
                activity.getString(title),
                activity.getString(message),
                activity.getString(okText),
                activity.getString(cancelText),
                okListener, cancelListener);
    }

    public static void showDialog(Activity activity, String title, String message, String okText, String cancelText,
                                  DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        if (okText != null) {
            builder.setPositiveButton(okText, okListener);
        }
        if (cancelText != null) {
            builder.setNegativeButton(cancelText, cancelListener);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
