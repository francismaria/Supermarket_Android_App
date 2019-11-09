package feup.mieic.cmov.acme.ui.order;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import feup.mieic.cmov.acme.R;

public class OrderFragment extends Fragment {

    private OrderViewModel orderViewModel;
    private Toast errorToast;

    private void initToast(){
        errorToast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
    }

    private void showErrorToast(String msg){
        errorToast.setText(msg);
        errorToast.show();
    }

    private void initActionBar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Purchase Information");
    }



    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

    }*/

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int orderID;

        initToast();
        initActionBar();

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            orderID = bundle.getInt("orderID", 0);
        } else {
            showErrorToast("Unavailable. Please try again later.");
            return null;
        }

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        orderViewModel.sendRequest(orderID);

        final View root = inflater.inflate(R.layout.fragment_order, container, false);

        orderViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                showErrorToast(s);
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderFragment.this.getActivity());
        final OrderAdapter adapter = new OrderAdapter(OrderFragment.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        orderViewModel.getLoad().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loadFinished) {
                if(loadFinished){
                    TextView orderIDText = root.findViewById(R.id.orderID);
                    TextView orderDateText = root.findViewById(R.id.orderDate);
                    TextView orderVouchersText = root.findViewById(R.id.orderVouchers);
                    TextView orderTotalCostText = root.findViewById(R.id.orderTotalCost);

                    adapter.updateProducts(orderViewModel.getProducts());

                    orderIDText.setText(orderViewModel.getOrderID());
                    orderDateText.setText(orderViewModel.getOrderDate());
                    orderVouchersText.setText(orderViewModel.getVouchers());
                    orderTotalCostText.setText(orderViewModel.getOrderTotalCost());
                }
            }
        });

        return root;
    }
}
