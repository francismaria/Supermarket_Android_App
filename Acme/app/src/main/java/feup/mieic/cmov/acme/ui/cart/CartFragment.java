package feup.mieic.cmov.acme.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.R;

public class CartFragment extends Fragment {

    // This will serve as a database to store the products
    private static List<String> cart = new ArrayList<>();

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Shopping Cart");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initActionBar();

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        return root;
    }

    public static void addProduct(String s){
        cart.add(s);
    }

    public static void printCardProducts(){
        for(String s : cart){
            Log.i("prod", s);
        }
    }
}
