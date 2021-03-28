package cool.zzy.sems.application.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cool.zzy.sems.application.R;

import java.util.List;

/**
 * @author intent <a>zzy.main@gmail.com</a>
 * @date 2021/3/20 23:29
 * @since 1.0
 */
public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final List<String> data;

    public DeliveryAdapter(List<String> data, Context context) {
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLinearLayout = itemView.findViewById(R.id.item_delivery_parent);
            parentLinearLayout.setOnClickListener((v) -> {
                Toast.makeText(itemView.getContext(), "该功能暂未实现！", Toast.LENGTH_LONG).show();
            });
        }
    }
}
