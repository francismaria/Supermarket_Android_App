package feup.mieic.cmov.acme.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.qrcodes.QRTag;
import feup.mieic.cmov.acme.ui.order.ProductModel;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Shopping Cart");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initActionBar();

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_cart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartFragment.this.getActivity());
        final CartAdapter adapter = new CartAdapter(CartFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        CartViewModel.getCart().observe(this, new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(@Nullable List<ProductModel> prods) {

                Log.e("update", "o");
                adapter.updateCartProducts(prods);
            }
        });

        root.findViewById(R.id.checkoutBtn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                byte[] c = new byte[10];

                startActivity(new Intent(CartFragment.this.getActivity(), QRTag.class).putExtra("data", c));
            }
        });

        return root;
    }



}
