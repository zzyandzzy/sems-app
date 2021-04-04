package cool.zzy.sems.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import cool.zzy.sems.application.R;
import cool.zzy.sems.application.util.DateUtils;
import cool.zzy.sems.context.model.Logistics;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/4/3 15:29
 * @since 1.0
 */
public class LogisticsAdapter extends RecyclerView.Adapter<LogisticsAdapter.ViewHolder> {
    private final List<Logistics> data;
    private final Context context;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public LogisticsAdapter(List<Logistics> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Logistics logistics = data.get(position);
        holder.created.setText("时间：" + DateUtils.formatDateTime(logistics.getCreated(), TimeUnit.MILLISECONDS));
        holder.info.setText("物流信息：" + logistics.getCurrentLocation());
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView created;
        private AppCompatTextView info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            created = itemView.findViewById(R.id.item_logistics_created);
            info = itemView.findViewById(R.id.item_logistics_info);
        }
    }
}
