package feup.mieic.cmov.acme.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.ui.cart.CartFragment;

public class HomeFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button readQRcodeBtn = root.findViewById(R.id.readQRbtn);

        final FragmentTransaction transaction = Objects.requireNonNull(this.getActivity()).getSupportFragmentManager().beginTransaction();
        final CartFragment cartFragment = new CartFragment();

        readQRcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.home_frame_container, cartFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }
}