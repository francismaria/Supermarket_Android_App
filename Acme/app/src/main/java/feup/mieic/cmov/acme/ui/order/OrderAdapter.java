package feup.mieic.cmov.acme.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.R;

public class OrderAdapter extends RecyclerView.Adapter {

    private List<ProductModel> prods;
    private OrderFragment fragmentParent;

    public OrderAdapter(OrderFragment fragment){
        this.prods = new ArrayList<>();
        this.fragmentParent = fragment;
    }

    public void updateProducts(List<ProductModel> info){
        this.prods.clear();
        this.prods.addAll(info);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String ID = prods.get(position).getID().toString();
        String name = prods.get(position).getName();
        String qty = prods.get(position).getQty();
        String price = prods.get(position).getPrice();

        ((OrderViewHolder) holder).bindView(ID, name, qty, price);
    }

    @Override
    public int getItemCount() {
        return prods.size();
    }

    private class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView idView;
        private TextView nameView;
        private TextView qtyView;
        private TextView priceView;

        public OrderViewHolder(View itemView){
            super(itemView);
            idView = itemView.findViewById(R.id.productID);
            nameView = itemView.findViewById(R.id.productName);
            qtyView = itemView.findViewById(R.id.productQty);
            priceView = itemView.findViewById(R.id.productPrice);
        }

        public void bindView(String ID, String name, String qty, String price){
            idView.setText(ID);
            nameView.setText(name);
            qtyView.setText(qty);
            priceView.setText(price);
        }
    }
}
