package cool.zzy.sems.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.util.DeliveryCompanyHandler;
import cool.zzy.sems.context.dto.DeliveryDTO;
import cool.zzy.sems.context.enums.DeliveryStatusEnum;
import cool.zzy.sems.context.model.DeliveryLogistics;
import cool.zzy.sems.context.model.Logistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/20 23:29
 * @since 1.0
 */
public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    public List<DeliveryLogistics> getData() {
        return data;
    }

    public void setData(List<DeliveryLogistics> data) {
        this.data = data;
    }

    private List<DeliveryLogistics> data;
    private final Context context;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

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
        setDeliveryLogisticsData(deliveryLogistics, this.context, holder.status, holder.info, holder.logistics, holder.avatar, true);
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        });
    }

    public static void setDeliveryLogisticsData(DeliveryLogistics deliveryLogistics, Context context,
                                                AppCompatTextView status, AppCompatTextView info, AppCompatTextView logistics, AppCompatImageView avatar,
                                                boolean isHideLongLogistics) {
        DeliveryDTO delivery = deliveryLogistics.getDelivery();
        // 快递信息
        StringBuilder infoStr = new StringBuilder();
        if (delivery.getDeliveryName() != null) {
            infoStr.append(delivery.getDeliveryName());
        }
        if (delivery.getRemark() != null) {
            infoStr.append("|").append(delivery.getRemark());
        }
        info.setText(infoStr.toString());
        // 快递状态
        DeliveryStatusEnum deliveryStatusEnum = DeliveryStatusEnum.from(delivery.getDeliveryStatus());
        status.setText(deliveryStatusEnum.getDescription());
        DeliveryCompanyHandler.DeliveryCompanyEntity deliveryCompanyEntity = DeliveryCompanyHandler.get(delivery.getDeliveryCompanyId());
        List<Logistics> logisticsList = deliveryLogistics.getLogisticsList();
        StringBuilder logisticsStr = new StringBuilder();
        if (deliveryCompanyEntity != null) {
            logisticsStr.append(deliveryCompanyEntity.getName()).append(":");
        }
        if (!logisticsList.isEmpty()) {
            // 物流信息
            logisticsStr.append(logisticsList.get(0).getDescription());
        } else {
            logisticsStr.append("暂无物流信息");
        }
        if (logisticsStr.length() > 35 && isHideLongLogistics) {
            logistics.setText(logisticsStr.substring(0, 35) + "...");
        } else {
            logistics.setText(logisticsStr.toString());
        }
        // 物流图片
        String avatarUrl = delivery.getAvatarUrl();
        if (avatarUrl != null) {
            Glide.with(context).load(avatarUrl).into(avatar);
        } else {
            if (deliveryCompanyEntity != null) {
                avatar.setImageResource(deliveryCompanyEntity.getAvatar());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //        private LinearLayout parentLinearLayout;
        private AppCompatTextView info;
        private AppCompatTextView logistics;
        private AppCompatImageView avatar;
        private AppCompatTextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            parentLinearLayout = itemView.findViewById(R.id.item_delivery_parent);
            info = itemView.findViewById(R.id.item_delivery_info);
            logistics = itemView.findViewById(R.id.item_delivery_logistics);
            avatar = itemView.findViewById(R.id.item_delivery_avatar);
            status = itemView.findViewById(R.id.item_delivery_status);
//            parentLinearLayout.setOnClickListener((v) -> {
//                Toast.makeText(itemView.getContext(), "该功能暂未实现！", Toast.LENGTH_LONG).show();
//            });
        }
    }
}
