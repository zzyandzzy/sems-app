package cool.zzy.sems.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import cool.zzy.sems.application.R;

/**
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 15:41
 * @since 1.0
 */
public class AuthorDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = AuthorDialog.class.getSimpleName();
    private AppCompatButton cancel;
    private AppCompatButton ok;

    public AuthorDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_author);
        setTitle(R.string.author);
        initViews();
        initData();
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    private void initData() {
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initViews() {
        cancel = findViewById(R.id.cancel);
        ok = findViewById(R.id.ok);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
            case R.id.cancel:
                dismiss();
                break;
            default:
        }
    }
}
