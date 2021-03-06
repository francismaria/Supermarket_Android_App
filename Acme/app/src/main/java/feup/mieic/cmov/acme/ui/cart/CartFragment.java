package feup.mieic.cmov.acme.ui.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.List;
import java.util.Objects;

import feup.mieic.cmov.acme.CheckoutActivity;
import feup.mieic.cmov.acme.HomeActivity;
import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.connection.VouchersCheckoutAction;
import feup.mieic.cmov.acme.qrcodes.QRTag;
import feup.mieic.cmov.acme.security.Cryptography;
import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;
import feup.mieic.cmov.acme.ui.order.ProductModel;

public class CartFragment extends Fragment {

    private Toast emptyCartToast;
    private CartViewModel cartViewModel;

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Shopping Cart");
    }

    private void initEmptyCartToast(){
        emptyCartToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
    }

    private void showEmptyCartToast(){
        emptyCartToast.setText("Your shopping cart is empty.");
        emptyCartToast.show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initActionBar();
        initEmptyCartToast();

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

            private static final int UUID_BYTES = 36;

            private JSONArray getProducts() throws JSONException {
                JSONArray arr = new JSONArray();
                List<ProductModel> prods = adapter.getCartProducts();

                for(ProductModel p : prods){
                    JSONObject item = new JSONObject();

                    item.put("productID", p.getID().toString());
                    item.put("productQty", p.getQty());

                    arr.put(item);
                }
                return arr;
            }

            @Override
            public void onClick(View v) {

                if(adapter.getItemCount() == 0){
                    showEmptyCartToast();
                    return;
                }

                try {
                    String uuid = SharedPrefsHolder.getUUID(Objects.requireNonNull(CartFragment.this.getActivity()));
                    int numVouchers = new VouchersCheckoutAction().execute(uuid).get();

                    JSONObject obj = new JSONObject();

                    obj.put("vouchers", numVouchers);
                    obj.put("prods", getProducts());

                    Intent intent = new Intent(CartFragment.this.getActivity(), CheckoutActivity.class);
                    intent.putExtra("data", obj.toString());
                    intent.putExtra("total_cost", adapter.getTotalPrice());

                    startActivity(intent);
                } catch(Exception e) {
                    // show toast error
                    e.printStackTrace();
                }

            }
        });

        return root;
    }
}
