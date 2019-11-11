package feup.mieic.cmov.acme;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import feup.mieic.cmov.acme.ui.cart.CartFragment;
import feup.mieic.cmov.acme.ui.contact.ContactFragment;
import feup.mieic.cmov.acme.ui.history.HistoryFragment;
import feup.mieic.cmov.acme.ui.home.HomeFragment;
import feup.mieic.cmov.acme.ui.order.OrderFragment;
import feup.mieic.cmov.acme.ui.order.OrderViewModel;
import feup.mieic.cmov.acme.ui.profile.ProfileFragment;
import feup.mieic.cmov.acme.ui.vouchers.VouchersFragment;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_history,
                R.id.nav_vouchers, R.id.nav_settings, R.id.nav_contact)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconShopBar:
/*
                Fragment cf = getSupportFragmentManager().findFragmentById(R.id.cart_frame_layout);
                if(cf != null && cf.isVisible()){
                    return true;
                }*/

                List<Fragment> fragments = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments();
                FragmentTransaction transaction = Objects.requireNonNull(this).getSupportFragmentManager().beginTransaction();
                CartFragment cartFragment = new CartFragment();

                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible()){
                        if(fragment instanceof VouchersFragment){
                            transaction.replace(R.id.vouchers_frame_container, cartFragment);
                        } else if(fragment instanceof ProfileFragment){
                            transaction.replace(R.id.profile_frame_container, cartFragment);
                        } else if(fragment instanceof HistoryFragment){
                            transaction.replace(R.id.history_frame_container, cartFragment);
                        } else if(fragment instanceof HomeFragment){
                            transaction.replace(R.id.home_frame_container, cartFragment);
                        } else if(fragment instanceof OrderFragment){
                            transaction.replace(R.id.order_frame_container, cartFragment);
                        } else if(fragment instanceof ContactFragment){
                            transaction.replace(R.id.contact_frame_container, cartFragment);
                        }
                        /* else if(fragment instanceof Settingsragment){
                            transaction.replace(R.id.settings_frame_container, cartFragment);
                        }*/
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
