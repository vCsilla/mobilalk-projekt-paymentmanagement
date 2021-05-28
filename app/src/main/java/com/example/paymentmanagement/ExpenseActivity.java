package com.example.paymentmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    private static final String LOG_TAG = ExpenseActivity.class.getName();
    Button edit, delete;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private CollectionReference mPayments = mFirestore.collection("Payments");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();;

    private RecyclerView mRecyclerView;
    private ArrayList<Payment> mPaymentList = new ArrayList<Payment>();
    private ExpenseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle("Card Payments");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationbar_filter);
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

        setUpRecyclerView();
    }
    private void setUpRecyclerView() {
        Query tmp = mPayments.whereEqualTo("userId", currentUserId).orderBy("name");
        Query query = tmp.whereEqualTo("paymentMethod", "Card");

        FirestoreRecyclerOptions<Payment> options = new FirestoreRecyclerOptions.Builder<Payment>()
                .setQuery(query, Payment.class).build();
        mAdapter = new ExpenseAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.expenseRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    public void displaySelectedListener(int itemId){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
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
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            mPaymentList.clear();
            setUpRecyclerView();
            mAdapter.notifyDataSetChanged();
        }
    }
}