package com.kikog.expensecontrol.view.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kikog.expensecontrol.R;
import com.kikog.expensecontrol.view.fragment.AddFragment;
import com.kikog.expensecontrol.view.fragment.DashboardFragment;

public class MainActivity extends AppCompatActivity {

    Fragment addFragment = new AddFragment();
    Fragment dashboardFragment = new DashboardFragment();
    Fragment activeFragment = addFragment;

    FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        onCreateFragmentManager();
    }

    private void onCreateFragmentManager(){
        mFragmentManager.beginTransaction().add(R.id.main_container, dashboardFragment, "2").hide(dashboardFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.main_container, addFragment, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    mFragmentManager.beginTransaction().hide(activeFragment).show(addFragment).commit();
                    activeFragment = addFragment;
                    return true;
                case R.id.navigation_dashboard:
                    mFragmentManager.beginTransaction().hide(activeFragment).show(dashboardFragment).commit();
                    activeFragment = dashboardFragment;
                    return true;
            }
            return false;
        }
    };

}
