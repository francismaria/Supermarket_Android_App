package feup.mieic.cmov.acme.ui.profile;

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

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        final TextView profileName = root.findViewById(R.id.profileNameText);
        final TextView profileUsername = root.findViewById(R.id.profileUsernameText);
        final TextView profileEmail = root.findViewById(R.id.profileEmailText);
        final TextView profileCCNr = root.findViewById(R.id.profileCCNrText);
        final TextView profileCCV = root.findViewById(R.id.profileCC_CCVText);
        final TextView profileExpDate = root.findViewById(R.id.profileCCExpDateText);

        profileViewModel.getNameTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileName.setText(s);
            }
        });

        profileViewModel.getUsernameTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileUsername.setText(s);
            }
        });

        profileViewModel.getEmailTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileEmail.setText(s);
            }
        });

        profileViewModel.getCCNumberTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileCCNr.setText(s);
            }
        });

        profileViewModel.getCCVTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileCCV.setText(s);
            }
        });

        profileViewModel.getExpDateTextView().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                profileExpDate.setText(s);
            }
        });

        return root;
    }
}