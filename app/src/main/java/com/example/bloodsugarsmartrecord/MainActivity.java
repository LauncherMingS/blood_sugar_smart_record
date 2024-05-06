package com.example.bloodsugarsmartrecord;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bloodsugarsmartrecord.databinding.ActivityMainBinding;
import com.example.bloodsugarsmartrecord.ui.dashboard.DashboardFragment;
import com.example.bloodsugarsmartrecord.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    //登出的UTF-8
    final private int LOGINOUT = 0xE7 + 0xE5;

    private SharedPreferences.Editor editor;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editor = getSharedPreferences("AccountData", Context.MODE_PRIVATE).edit();
        editor.putString("loginStatus", "");
        editor.apply();

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        BottomNavigationView navView = binding.navView;

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                int id = menuItem.getItemId();
                if (id == R.id.navigation_home) {
                    setTitle("A");
                    selectedFragment = new HomeFragment();
                }
                else if (id == R.id.navigation_dashboard) {
                    setTitle("B");
                    selectedFragment = new DashboardFragment();
                }

                if (selectedFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(id, selectedFragment).commit();

                return true;
            }
        });

        NavigationUI.setupWithNavController(navView, navController);
        navView.setVisibility(View.INVISIBLE);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(0, LOGINOUT, 0, "登出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == LOGINOUT) {
            //隱藏底部導覽
            findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
            //清除登入狀態
            editor.putString("loginStatus", "");
            editor.apply();
            navController.navigate(R.id.navigation_login);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.putString("loginStatus", "");
        editor.apply();
    }
}