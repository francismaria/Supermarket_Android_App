package feup.mieic.cmov.acme.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import feup.mieic.cmov.acme.security.KeyInstance;
import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.cart.CartFragment;
import feup.mieic.cmov.acme.ui.cart.CartViewModel;
import feup.mieic.cmov.acme.ui.order.ProductModel;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button readQRcodeBtn = root.findViewById(R.id.readQRbtn);

        final FragmentTransaction transaction = Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction();
        final CartFragment cartFragment = new CartFragment();

        readQRcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open QR CODE READER
                CartViewModel.addProduct(new ProductModel("1", "exa", "1", "10"));
            }
        });

        try{
            SharedPreferences settings = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

            //KeyInstance.setKeyname(settings.getString("current_user", null));

        } catch(Exception e){
            e.printStackTrace();
        }

        return root;
    }
}