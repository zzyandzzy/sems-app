package cool.zzy.sems.application.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.SemsApplication;
import cool.zzy.sems.context.dto.UserDTO;
import cool.zzy.sems.context.model.User;
import cool.zzy.sems.context.service.UserService;

/**
 * xyz.zzyitj.iface.ui
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 15:41
 * @since 1.0
 */
public class UpdateUserInfoDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = UpdateUserInfoDialog.class.getSimpleName();
    private Context context;
    private final User user;
    private AppCompatEditText email;
    private AppCompatEditText username;
    private AppCompatCheckBox gender;
    private AppCompatButton cancel;
    private AppCompatButton ok;

    public UpdateUserInfoDialog(@NonNull Context context, User user) {
        super(context);
        this.context = context;
        this.user = user;
        setContentView(R.layout.dialog_user_info_update);
        setTitle(R.string.update_user);
        initViews();
        initData();
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    private void initData() {
        email.setText(user.getEmail());
        username.setText(user.getNickname());
        gender.setChecked(user.getGender());
        if (gender.isChecked()) {
            gender.setText(R.string.man);
        } else {
            gender.setText(R.string.woman);
        }
        gender.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gender.setText(R.string.man);
            } else {
                gender.setText(R.string.woman);
            }
        });
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initViews() {
        email = findViewById(R.id.dialog_user_info_update_email);
        username = findViewById(R.id.dialog_user_info_update_user_name);
        gender = findViewById(R.id.dialog_user_info_update_gender);
        cancel = findViewById(R.id.dialog_user_info_update_cancel);
        ok = findViewById(R.id.dialog_user_info_update_ok);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_user_info_update_ok:
                updateUser();
                break;
            case R.id.dialog_user_info_update_cancel:
                dismiss();
                break;
        }
    }

    private void updateUser() {
        UserService userService = SemsApplication.instance.getUserService();
        User updateUser = new User();
        updateUser.setEmail(user.getEmail());
        updateUser.setGender(gender.isChecked());
        updateUser.setNickname(username.getText().toString());
        UserDTO userDTO = userService.updateUser(updateUser);
        if (userDTO == null) {
            Toast.makeText(context, R.string.modify_user_fail, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show();
            SemsApplication.instance.putUser(userDTO);
            dismiss();
        }
    }
}
