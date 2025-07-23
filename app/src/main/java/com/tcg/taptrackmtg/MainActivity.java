package com.tcg.taptrackmtg;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.work.Configuration;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tcg.taptrackmtg.fragment.LifetapFragment;
import com.tcg.taptrackmtg.fragment.TrackedCardsFragment;
import com.tcg.taptrackmtg.fragment.TrackingFragment;
import com.tcg.taptrackmtg.worker.PriceCheckWorker;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    private FirebaseUser currentUser;

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            currentUser = (FirebaseUser) bundle.getParcelable("user");
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, TrackingFragment.newInstance(currentUser))
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

        try {
            Picasso.setSingletonInstance(new Picasso.Builder(getApplicationContext()).build());
        } catch (IllegalStateException e) {
            // Ya estaba instanciado, no pasa nada
            Log.w("Picasso", "Singleton instance already set, skipping.");
        }
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
                fragment = TrackingFragment.newInstance(currentUser);
            } else if (itemId == R.id.nav_lifetap) {
                Toast.makeText(this, "Lifetap", Toast.LENGTH_SHORT).show();
                fragment = LifetapFragment.newInstance();
            } else if (itemId == R.id.nav_trackedcards) {
                fragment = TrackedCardsFragment.newInstance(currentUser);
            }else {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
        if(!WorkManager.isInitialized()) {
            WorkManager.initialize(this, new Configuration.Builder().build());
        }
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