package feup.mieic.cmov.acme.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import feup.mieic.cmov.acme.R;

public class CartFragment extends Fragment {

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Shopping Cart");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initActionBar();
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        return root;
    }
}
