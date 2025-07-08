package com.tcg.empires;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.tcg.empires.fragment.LifetapFragment;
import com.tcg.empires.fragment.TrackingFragment;
import com.tcg.empires.worker.PriceCheckWorker;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fragmentContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, TrackingFragment.newInstance())
                    .commitNow();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        enqueuePriceCheckWorks();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_pricetracking) {
                fragment = TrackingFragment.newInstance();
            } else if (itemId == R.id.nav_lifetap) {
                Toast.makeText(this, "Lifetap", Toast.LENGTH_SHORT).show();
                fragment = LifetapFragment.newInstance();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


    }

    private void enqueuePriceCheckWorks() {
        Constraints internetRequired = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        PeriodicWorkRequest priceCheckDaily = new PeriodicWorkRequest.Builder(PriceCheckWorker.class, 1, TimeUnit.DAYS).setInputData(new Data.Builder().putInt("period",3).build()).setConstraints(internetRequired).build();
        PeriodicWorkRequest priceCheckWeekly = new PeriodicWorkRequest.Builder(PriceCheckWorker.class, 1, TimeUnit.DAYS).setInputData(new Data.Builder().putInt("period",4).build()).setConstraints(internetRequired).build();
        PeriodicWorkRequest priceCheckMonthly = new PeriodicWorkRequest.Builder(PriceCheckWorker.class, 1, TimeUnit.DAYS).setInputData(new Data.Builder().putInt("period",5).build()).setConstraints(internetRequired).build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "daily_check",
                ExistingPeriodicWorkPolicy.KEEP,
                priceCheckDaily
        );
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "weekly_check",
                ExistingPeriodicWorkPolicy.KEEP,
                priceCheckWeekly
        );
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(
                "monthly_check",
                ExistingPeriodicWorkPolicy.KEEP,
                priceCheckMonthly
        );
    }
}