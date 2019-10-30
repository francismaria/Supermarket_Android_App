package feup.mieic.cmov.acme.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import feup.mieic.cmov.acme.R;

public class HistoryAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HistoryViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return 2;
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

        public void bindView(int position){
            totalView.setText("ajajajajaja");
            dateView.setText("okokoko");
        }

        public void onClick(View view){

        }
    }
}
