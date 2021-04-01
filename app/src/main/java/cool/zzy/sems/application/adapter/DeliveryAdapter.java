package cool.zzy.sems.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.context.model.Delivery;
import cool.zzy.sems.context.model.DeliveryLogistics;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/20 23:29
 * @since 1.0
 */
public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final List<DeliveryLogistics> data;

    public DeliveryAdapter(List<DeliveryLogistics> data, Context context) {
        this.data = data;
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
        Delivery delivery = deliveryLogistics.getDelivery();
        StringBuilder info = new StringBuilder();
        if (delivery.getDeliveryName() != null) {
            info.append(delivery.getDeliveryName());
        }
        if (delivery.getRemark() != null) {
            info.append("|").append(delivery.getRemark());
        }
        holder.info.setText(info.toString());
        String logistics = deliveryLogistics.getDeliveryCompany().getCompanyName() + ":"
                + deliveryLogistics.getLogisticsList().get(0).getCurrentLocation();
        if (logistics.length() > 30) {
            logistics = logistics.substring(30) + "...";
        }
        holder.logistics.setText(logistics);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentLinearLayout;
        private AppCompatTextView info;
        private AppCompatTextView logistics;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLinearLayout = itemView.findViewById(R.id.item_delivery_parent);
            info = itemView.findViewById(R.id.item_delivery_info);
            logistics = itemView.findViewById(R.id.item_delivery_logistics);
            parentLinearLayout.setOnClickListener((v) -> {
                Toast.makeText(itemView.getContext(), "该功能暂未实现！", Toast.LENGTH_LONG).show();
            });
        }
    }
}
