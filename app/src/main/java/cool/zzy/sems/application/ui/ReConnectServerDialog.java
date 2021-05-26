package cool.zzy.sems.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.application.constant.Const;

/**
 * xyz.zzyitj.iface.ui
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 15:41
 * @since 1.0
 */
public class ReConnectServerDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = ReConnectServerDialog.class.getSimpleName();
    private Context context;
    private AppCompatEditText serverHost;
    private AppCompatButton cancel;
    private AppCompatButton ok;

    public ReConnectServerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_re_connect_server);
        setTitle("设置服务器");
        initViews();
        initData();
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    private void initViews() {
        serverHost = findViewById(R.id.server_host);
        cancel = findViewById(R.id.dialog_user_info_update_cancel);
        ok = findViewById(R.id.dialog_user_info_update_ok);
    }

    private void initData() {
        serverHost.setText(Const.RPC_IP);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_user_info_update_ok:
                Const.RPC_IP = serverHost.getText().toString();
                SemsApplication.instance.initRPC();
                dismiss();
                break;
            case R.id.dialog_user_info_update_cancel:
                dismiss();
                break;
        }
    }
}
