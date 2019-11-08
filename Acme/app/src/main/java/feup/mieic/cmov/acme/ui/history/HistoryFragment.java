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

    private HistoryViewModel historyViewModel;


    //final TextView textView = root.findViewById(R.id.text_slideshow);



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_history, container, false);
/*
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_history);


        List<ItemModel> items = new ArrayList<>();
        items.add(new ItemModel("25408", "300€", "27/10/2019"));
        items.add(new ItemModel("22012", "560,20€", "22/10/2019"));
        items.add(new ItemModel("19034", "450,12€", "21/10/2019"));
        items.add(new ItemModel("16790", "450,12€", "21/10/2019"));
        items.add(new ItemModel("15402", "450,12€", "21/10/2019"));
        items.add(new ItemModel("12313","450,12€", "21/10/2019"));
        items.add(new ItemModel("00002","450,12€", "21/10/2019"));
        items.add(new ItemModel("00001","450,12€", "21/10/2019"));

        HistoryAdapter adapter = new HistoryAdapter(items, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());

        historyViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //
                //recyclerView.setLayoutManager(layoutManager);

                //recyclerView.setAdapter(adapter);
            }
        });
*/
        return root;
    }

}