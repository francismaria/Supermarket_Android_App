package feup.mieic.cmov.acme.ui.help;

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

public class HelpFragment extends Fragment {

    private HelpModel helpModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpModel =
                ViewModelProviders.of(this).get(HelpModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        helpModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}