package feup.mieic.cmov.acme.ui.history;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import feup.mieic.cmov.acme.R;

public class HistoryFragment extends Fragment {

    //private HistoryViewModel historyViewModel;


    //historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);

        historyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_history);


        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("300€", "27/10/2019"));
        items.add(new ItemModel("560,20€", "22/10/2019"));
        items.add(new ItemModel("450,12€", "21/10/2019"));

        HistoryAdapter adapter = new HistoryAdapter(items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return root;
    }
}