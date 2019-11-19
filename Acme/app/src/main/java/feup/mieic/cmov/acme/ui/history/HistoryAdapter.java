package feup.mieic.cmov.acme.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.order.OrderFragment;

public class HistoryAdapter extends RecyclerView.Adapter {

    private List<ItemModel> items;
    private HistoryFragment fragmentParent;

    public HistoryAdapter(List<ItemModel> items, HistoryFragment fragment){
        if(items == null){
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
        this.fragmentParent = fragment;
    }

    public void updateItemsList(List<ItemModel> items){
        this.items.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
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
        String id = items.get(position).getID();

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

        private String orderID;

        public HistoryViewHolder(View itemView){
            super(itemView);
            orderID = null;
            totalView = itemView.findViewById(R.id.total_history);
            dateView = itemView.findViewById(R.id.date_history);
            orderIDView = itemView.findViewById(R.id.order_id_history);

            itemView.setOnClickListener(this);
        }

        public void bindView(String total, String date, String id){
            totalView.setText(total);
            dateView.setText(date);
            orderIDView.setText("#" + id);

            orderID = id;
        }

        public void onClick(View view){
            OrderFragment orderFragment = new OrderFragment();

            Bundle args = new Bundle();
            args.putString("orderID", orderID);

            orderFragment.setArguments(args);

            FragmentTransaction transaction = Objects.requireNonNull(fragmentParent.getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.history_frame_container, orderFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
