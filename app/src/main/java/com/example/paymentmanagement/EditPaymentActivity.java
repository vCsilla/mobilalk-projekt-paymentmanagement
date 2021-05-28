package com.example.paymentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText paymentNameTIL, paymentAmountTIL, paymentDescriptionTIL;
    Spinner spinner, unitSpinner;
    RadioGroup paymentStatusRG;
    BottomNavigationView bottomNavigationView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.myToolbar);
        toolbar.setTitle("Payment Editor");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Payment extra= intent.getParcelableExtra("payment");

        paymentNameTIL = findViewById(R.id.editPaymentNameInput);
        paymentAmountTIL = findViewById(R.id.editPaymentAmountInput);
        unitSpinner = (Spinner) findViewById(R.id.editPaymentAmountUnitSpinner);
        unitSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapterU = ArrayAdapter.createFromResource(this,
                R.array.payment_amount_units, R.layout.spinner);
        adapterU.setDropDownViewResource(R.layout.spinner_dropdown);
        unitSpinner.setAdapter(adapterU);

        spinner = (Spinner) findViewById(R.id.editPaymentMethodSpinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);

        paymentDescriptionTIL = findViewById(R.id.editPaymentDescriptionInput);
        paymentStatusRG = findViewById(R.id.editPaymentStatusRadioGroup);
        paymentStatusRG.check(R.id.editPaymentStatusPendingRB);

        paymentNameTIL.setText(extra.getName());
        String[] amount = extra.getAmount().toString().split(" ");
        paymentAmountTIL.setText(amount[1]);
        //method
        paymentDescriptionTIL.setText(extra.getDescription());
        //status
        currentUserId = user.getUid();

        findViewById(R.id.editPaymentButton).setOnClickListener(v -> {
            Payment payment = makePayment();
            if (payment == null) {
                Toast.makeText(EditPaymentActivity.this, "Must fill them!", Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseFirestore.getInstance().collection("Payments").document(extra.getFirebaseId()).set(payment);
            Intent intent2 = new Intent(getBaseContext(), PaymentManagingActivity.class);
            startActivity(intent2);
            Toast.makeText(EditPaymentActivity.this, "Payment Edited", Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.cancelEditPaymentButton).setOnClickListener(v -> {
            Intent intent1 = new Intent(getBaseContext(), PaymentManagingActivity.class);
            startActivity(intent1);
        });

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
}