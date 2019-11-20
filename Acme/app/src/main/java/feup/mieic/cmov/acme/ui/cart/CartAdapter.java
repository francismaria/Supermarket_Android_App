package feup.mieic.cmov.acme.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.order.ProductModel;

public class CartAdapter extends RecyclerView.Adapter {

    private List<ProductModel> prods;
    private CartFragment fragmentParent;

    public CartAdapter(CartFragment fragment){
        this.prods = new ArrayList<>();
        this.fragmentParent = fragment;
    }

    public void updateCartProducts(List<ProductModel> prods){
        this.prods.clear();
        this.prods.addAll(prods);
        this.notifyDataSetChanged();
    }

    public List<ProductModel> getCartProducts(){
        return prods;
    }

    public double getTotalPrice(){
        double total = 0;

        for(ProductModel p: prods){
            Double price = Double.parseDouble(p.getPrice());
            int qty = Integer.parseInt(p.getQty());
            total +=  price*qty;
        }

        return total;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String ID = prods.get(position).getID().toString();
        String name = prods.get(position).getName();
        String qty = prods.get(position).getQty();
        String price = prods.get(position).getPrice();

        ((CartItemsViewHolder) holder).bindView(ID, name, qty, price);
    }

    @Override
    public int getItemCount() {
        return prods.size();
    }

    private class CartItemsViewHolder extends RecyclerView.ViewHolder {

        private final TextView idView;
        private TextView nameView;
        private TextView qtyView;
        private TextView priceView;

        public CartItemsViewHolder(View itemView){
            super(itemView);
            idView = itemView.findViewById(R.id.cartProdID);
            nameView = itemView.findViewById(R.id.cartProdName);
            qtyView = itemView.findViewById(R.id.cartProdQty);
            priceView = itemView.findViewById(R.id.cartProdPrice);
        }

        public void bindView(String ID, String name, String qty, String price){
            idView.setText(ID);
            nameView.setText(name);
            qtyView.setText(qty);
            priceView.setText(price);

            itemView.findViewById(R.id.removeProdBtn).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    CartViewModel.removeProduct(idView.getText().toString());
                }
            });

        }
    }
}
