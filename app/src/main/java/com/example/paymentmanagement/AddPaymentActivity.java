package com.example.paymentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = AddPaymentActivity.class.getName();

    TextInputEditText paymentNameTIL, paymentAmountTIL, paymentDescriptionTIL;
    Spinner spinner, unitSpinner;
    RadioGroup paymentStatusRG;
    BottomNavigationView bottomNavigationView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;

    private NotificationHandler mNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle("Payment Adder");
        setSupportActionBar(toolbar);

        paymentNameTIL = findViewById(R.id.paymentNameInput);
        paymentAmountTIL = findViewById(R.id.paymentAmountInput);
        unitSpinner = (Spinner) findViewById(R.id.paymentAmountUnitSpinner);
        unitSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterU = ArrayAdapter.createFromResource(this,
                R.array.payment_amount_units, R.layout.spinner);
        adapterU.setDropDownViewResource(R.layout.spinner_dropdown);
        unitSpinner.setAdapter(adapterU);
        spinner = (Spinner) findViewById(R.id.paymentMethodSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
        paymentDescriptionTIL = findViewById(R.id.paymentDescriptionInput);
        paymentStatusRG = findViewById(R.id.paymentStatusRadioGroup);
        paymentStatusRG.check(R.id.paymentStatusPendingRB);
        currentUserId = user.getUid();

        findViewById(R.id.addPaymentButton).setOnClickListener(v -> {
            Payment payment = makePayment();
            Log.e(LOG_TAG,payment.toString());
            if(payment == null){
                Toast.makeText(AddPaymentActivity.this, "Must fill everything out!", Toast.LENGTH_LONG).show();
            }
            FirebaseFirestore.getInstance().collection("Payments").add(payment);
            Toast.makeText(AddPaymentActivity.this, "Payment Added", Toast.LENGTH_LONG).show();

            Intent intent2 = new Intent(getBaseContext(), PaymentManagingActivity.class);
            startActivity(intent2);

            mNotificationHandler.send(payment.getName());
        });
        findViewById(R.id.cancelPaymentButton).setOnClickListener(v -> {
            Intent intent1 = new Intent(getBaseContext(), PaymentManagingActivity.class);
            startActivity(intent1);
        });

        mNotificationHandler = new NotificationHandler(this);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationbar2);
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
    }

    private Payment makePayment(){
        String paymentName = paymentNameTIL.getText().toString().trim();
        String paymentAmountUnit = unitSpinner.getSelectedItem().toString();
        float paymentAmountValue = Float.parseFloat(paymentAmountTIL.getText().toString().trim());
        Money paymentAmount = new Money(paymentAmountUnit, paymentAmountValue);
        String paymentMethod = spinner.getSelectedItem().toString();
        String paymentDescription = paymentDescriptionTIL.getText().toString().trim();
        int checkedId = paymentStatusRG.getCheckedRadioButtonId();
        RadioButton radioButton = paymentStatusRG.findViewById(checkedId);
        String paymentStatus = radioButton.getText().toString();

        return new Payment(paymentName,paymentAmount,paymentMethod,paymentDescription,paymentStatus,currentUserId);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}