package feup.mieic.cmov.acme.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import feup.mieic.cmov.acme.R;

public class HistoryFragment extends Fragment {

    private Toast errorToast;
    private HistoryViewModel historyViewModel;

    private void initToast(){
        errorToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
    }

    private void showErrorToast(String msg){
        errorToast.setText(msg);
        errorToast.show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initToast();
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_history, container, false);

        historyViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showErrorToast(s);
            }
        });


        // TODO : pass this to the observer function to ensure that there are no memory leaks


        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryFragment.this.getActivity());
        final HistoryAdapter adapter = new HistoryAdapter(historyViewModel.getItems(), HistoryFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        historyViewModel.getLoad().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loadFinished) {

                if(loadFinished){
                    adapter.updateItemsList(historyViewModel.getItems());
                }
            }
        });

        return root;
    }

}