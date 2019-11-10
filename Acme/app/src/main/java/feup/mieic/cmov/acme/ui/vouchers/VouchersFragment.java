package feup.mieic.cmov.acme.ui.vouchers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import feup.mieic.cmov.acme.R;

public class VouchersFragment extends Fragment {

    private VouchersViewModel vouchersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vouchersViewModel = ViewModelProviders.of(this).get(VouchersViewModel.class);


        View root = inflater.inflate(R.layout.fragment_vouchers, container, false);

        final TextView vouchersMsg = root.findViewById(R.id.text_vouchers);
        final TextView noVouchersMsg = root.findViewById(R.id.textNoVoucherDiff);

        vouchersViewModel.getLoad().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean success) {
                if(success) {
                    if(vouchersViewModel.isRequestSuccessful()){
                        String numStr = Integer.toString(vouchersViewModel.getVouchersNum());
                        vouchersMsg.setText("You have vouchers " + numStr + " to discount on your next purchase!");
                    } else {
                        noVouchersMsg.setText("Currently, you have ... € accumulated.\nYou are just ...€ short!");
                    }
                } else {
                    // show error toast
                }
            }
        });

        return root;
    }
}



