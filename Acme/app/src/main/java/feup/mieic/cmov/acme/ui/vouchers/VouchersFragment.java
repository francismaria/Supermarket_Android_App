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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vouchersViewModel =
                ViewModelProviders.of(this).get(VouchersViewModel.class);


        View root = inflater.inflate(R.layout.fragment_vouchers, container, false);
        final TextView textView = root.findViewById(R.id.text_vouchers);

        vouchersViewModel.getText().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer stock) {
                textView.setText(Integer.toString(stock));
            }
        });
        return root;
    }
}



