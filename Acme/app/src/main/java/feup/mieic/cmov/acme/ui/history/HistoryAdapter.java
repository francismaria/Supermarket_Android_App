package feup.mieic.cmov.acme.ui.history;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.R;

public class HistoryAdapter extends RecyclerView.Adapter {

    private List<ItemModel> items;

    public HistoryAdapter(List<ItemModel> items){
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HistoryViewHolder) holder).bindView(items.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView dateView;
        private TextView totalView;

        public HistoryViewHolder(View itemView){
            super(itemView);
            totalView = (TextView) itemView.findViewById(R.id.total_history);
            dateView = (TextView) itemView.findViewById(R.id.date_history);

            itemView.setOnClickListener(this);
        }

        public void bindView(String total){
            totalView.setText(total);
            dateView.setText("okokoko");
        }

        public void onClick(View view){
            Log.i("history_item:" , "clicked");
        }
    }
}
