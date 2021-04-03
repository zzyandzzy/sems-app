package cool.zzy.sems.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import cool.zzy.sems.application.R;
import cool.zzy.sems.context.dto.DeliveryDTO;
import cool.zzy.sems.context.enums.DeliveryStatusEnum;
import cool.zzy.sems.context.model.DeliveryLogistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/20 23:29
 * @since 1.0
 */
public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final List<DeliveryLogistics> data;
    private final Context context;

    public DeliveryAdapter(List<DeliveryLogistics> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeliveryLogistics deliveryLogistics = data.get(position);
        DeliveryDTO delivery = deliveryLogistics.getDelivery();
        // 快递信息
        StringBuilder info = new StringBuilder();
        if (delivery.getDeliveryName() != null) {
            info.append(delivery.getDeliveryName());
        }
        if (delivery.getRemark() != null) {
            info.append("|").append(delivery.getRemark());
        }
        holder.info.setText(info.toString());
        // 快递状态
        DeliveryStatusEnum deliveryStatusEnum = DeliveryStatusEnum.of(delivery.getDeliveryStatus());
        holder.status.setText(deliveryStatusEnum.getDescription());
        // 物流信息
        String logistics = deliveryLogistics.getDeliveryCompany().getCompanyName() + ":"
                + deliveryLogistics.getLogisticsList().get(0).getCurrentLocation();
        if (logistics.length() > 30) {
            logistics = logistics.substring(0, 30) + "...";
        }
        holder.logistics.setText(logistics);
        // 物流图片
        String avatarUrl = delivery.getAvatarUrl();
        if (avatarUrl == null) {
            avatarUrl = deliveryLogistics.getDeliveryCompany().getAvatarUrl();
        }
        Glide.with(this.context).load(avatarUrl).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentLinearLayout;
        private AppCompatTextView info;
        private AppCompatTextView logistics;
        private AppCompatImageView avatar;
        private AppCompatTextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLinearLayout = itemView.findViewById(R.id.item_delivery_parent);
            info = itemView.findViewById(R.id.item_delivery_info);
            logistics = itemView.findViewById(R.id.item_delivery_logistics);
            avatar = itemView.findViewById(R.id.item_delivery_avatar);
            status = itemView.findViewById(R.id.item_delivery_status);
            parentLinearLayout.setOnClickListener((v) -> {
                Toast.makeText(itemView.getContext(), "该功能暂未实现！", Toast.LENGTH_LONG).show();
            });
        }
    }
}
