package feup.mieic.cmov.acme.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PointerIcon;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.qrcodes.QRTag;
import feup.mieic.cmov.acme.security.Cryptography;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;
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
                adapter.updateCartProducts(prods);
            }
        });

        root.findViewById(R.id.checkoutBtn).setOnClickListener(new View.OnClickListener(){

            private JSONArray arr;

            private void getProducts() throws JSONException {
                List<ProductModel> prods = adapter.getCartProducts();

                for(ProductModel p : prods){
                    JSONObject item = new JSONObject();

                    item.put("productID", p.getID());
                    item.put("productName", p.getName());
                    item.put("productPrice", p.getPrice());
                    item.put("productQty", p.getQty());

                    arr.put(item);
                }
            }

            @Override
            public void onClick(View v) {


                if(adapter.getItemCount() == 0)
                    return; // TODO show toast saying cart is null

                try {
                    JSONObject obj = new JSONObject();
                    arr = new JSONArray();

                    getProducts();

                    obj.put("prods", arr);
                    //byte[] info = Cryptography.encrypt(obj.toString(), KeyInstance.getPrivateKey());

                    Log.e("arr", obj.toString());

                    startActivity(new Intent(CartFragment.this.getActivity(), QRTag.class).putExtra("data", obj.toString().getBytes(StandardCharsets.ISO_8859_1)));
                } catch(Exception e){
                    // show toast error
                    e.printStackTrace();
                }
            }
        });

        return root;
    }



}
