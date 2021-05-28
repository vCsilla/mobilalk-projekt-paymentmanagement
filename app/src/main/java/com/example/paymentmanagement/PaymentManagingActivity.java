package com.example.paymentmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PaymentManagingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = PaymentManagingActivity.class.getName();
    private final int REQUEST_CODE_ASK_PERMISSIONS = 28;
    BottomNavigationView bottomNavigationView;
    Vibrator vibrator;
    Button deleteBtn;

    private FirebaseFirestore mFirestore;
    private CollectionReference mPayments;
    private String currentUserId;


    private RecyclerView mRecyclerView;
    private ArrayList<Payment> mPaymentList;
    private PaymentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_managing);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle("Payment Manager");
        setSupportActionBar(toolbar);

        checkPermission();
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationbar);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_bottom:
                        Intent intent = new Intent(getBaseContext(), PaymentManagingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.expense_bottom:
                        Intent in = new Intent(getBaseContext(), ExpenseActivity.class);
                        startActivity(in);
                        break;
                }
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }currentUserId = user.getUid();

        Log.d(LOG_TAG, currentUserId);

        mFirestore = FirebaseFirestore.getInstance();
        mPayments = mFirestore.collection("Payments");

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPaymentList = new ArrayList<>();
        mAdapter = new PaymentAdapter(this, mPaymentList);
        mRecyclerView.setAdapter(mAdapter);

        queryData();
    }
    private void queryData() {
        mPayments.whereEqualTo("userId", currentUserId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Payment item = document.toObject(Payment.class);
                item.setFirebaseId(document.getId());
                mPaymentList.add(item);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    public void add(View view) {
        Intent intent = new Intent(this, AddPaymentActivity.class);
        startActivity(intent);
        vibrate();
    }
    public void filter(View view){
        Intent intent = new Intent(this, ExpenseActivity.class);
        startActivity(intent);
    }
    public void checkPermission(){
        if(Build.VERSION.SDK_INT >= 24){
            if(ActivityCompat.checkSelfPermission(PaymentManagingActivity.this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.VIBRATE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
    }
    public void vibrate(){
        if(Build.VERSION.SDK_INT >= 26){
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            vibrator.vibrate(100);
        }
    }

    public void displaySelectedListener(int itemId){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedListener((item.getItemId()));
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.home){
            Intent intent = new Intent(this, PaymentManagingActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.expense){
            Intent intent = new Intent(this, ExpenseActivity.class);
            startActivity(intent);
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //return true;
        }
        if(item.getItemId() == R.id.home){
            Intent intent = new Intent(this, PaymentManagingActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.expense){
            Intent intent = new Intent(this, ExpenseActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            mPaymentList.clear();
            queryData();
            mAdapter.notifyDataSetChanged();
        }
    }
}