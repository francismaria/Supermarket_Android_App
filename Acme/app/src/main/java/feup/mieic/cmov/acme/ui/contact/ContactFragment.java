package feup.mieic.cmov.acme.ui.contact;

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

public class ContactFragment extends Fragment {

    private ContactModel contactModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactModel =
                ViewModelProviders.of(this).get(ContactModel.class);
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        contactModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}