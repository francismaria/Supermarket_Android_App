package feup.mieic.cmov.acme.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import java.util.Objects;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.order.OrderFragment;

public class HistoryAdapter extends RecyclerView.Adapter {

    private List<ItemModel> items;
    private HistoryFragment fragmentParent;

    public HistoryAdapter(List<ItemModel> items, HistoryFragment fragment){
        this.items = items;
        this.fragmentParent = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String total = items.get(position).getTotal();
        String date = items.get(position).getDate();
        String id = "#" + items.get(position).getID();

        ((HistoryViewHolder) holder).bindView(total, date, id);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView dateView;
        private TextView totalView;
        private TextView orderIDView;

        public HistoryViewHolder(View itemView){
            super(itemView);
            totalView = (TextView) itemView.findViewById(R.id.total_history);
            dateView = (TextView) itemView.findViewById(R.id.date_history);
            orderIDView = (TextView) itemView.findViewById(R.id.order_id_history);

            itemView.setOnClickListener(this);
        }

        public void bindView(String total, String date, String id){
            totalView.setText(total);
            dateView.setText(date);
            orderIDView.setText(id);
        }

        public void onClick(View view){
            Log.i("history_item:" , "clicked");

            OrderFragment orderFragment = new OrderFragment();

            Bundle args = new Bundle();
            args.putString("data", "This data has sent to FragmentTwo");

            orderFragment.setArguments(args);

            FragmentTransaction transaction = Objects.requireNonNull(fragmentParent.getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, orderFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
