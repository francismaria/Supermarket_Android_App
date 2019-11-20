package feup.mieic.cmov.acme.ui.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import feup.mieic.cmov.acme.R;
import feup.mieic.cmov.acme.security.SharedPrefsHolder;


public class ContactFragment extends Fragment {

    private Toast unavailableToast;
    private Toast notAuthToast;
    private Toast emptyMsgToast;
    private ContactViewModel contactViewModel;

    private void initToasts(){
        unavailableToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
        notAuthToast =  Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
        emptyMsgToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
    }

    private void showNotAuthToast(){
        notAuthToast.setText("Please wait until the message is sent");
        notAuthToast.show();
    }

    private void showUnavailableToast(){
        unavailableToast.setText("Unavailable. Please try again later.");
        unavailableToast.show();
    }

    private void showEmptyMsgToast(){
        emptyMsgToast.setText("Your message is empty.");
        emptyMsgToast.show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initToasts();
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_contact, container, false);

        root.findViewById(R.id.successMsg).setVisibility(View.INVISIBLE);

        // OnClick -> send button

        Button button = root.findViewById(R.id.sendMsgBtn);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String msg = ((EditText)root.findViewById(R.id.inputMsg)).getText().toString();

                if(!contactViewModel.isRequestFinished()){      // ongoing request
                    showNotAuthToast();
                } else if (msg.equals("")){         // message is empty
                    showEmptyMsgToast();
                } else {
                    contactViewModel.setUUID(SharedPrefsHolder.getUUID(Objects.requireNonNull(getActivity())));
                    contactViewModel.sendMessage(msg);
                }
            }
        });

        // Update UI

        final Button sendBtn = root.findViewById(R.id.sendMsgBtn);

        contactViewModel.getRequestResult().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean res) {
                if(res){
                    sendBtn.setVisibility(View.GONE);
                    root.findViewById(R.id.successMsg).setVisibility(View.VISIBLE);
                }
                else{
                    showUnavailableToast();
                }
            }
        });

        return root;
    }
}
