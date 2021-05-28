package com.example.paymentmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<Payment, ExpenseAdapter.ExpenseHolder> {

    public ExpenseAdapter(FirestoreRecyclerOptions<Payment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(ExpenseHolder expenseHolder, int i, Payment payment) {
        expenseHolder.mName.setText(payment.getName());
        expenseHolder.mAmount.setText(payment.getAmount().toString());
        expenseHolder.mMethod.setText(payment.getPaymentMethod());
        expenseHolder.mDescription.setText(payment.getDescription());
        expenseHolder.mStatus.setText(payment.getStatus());
    }

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_layout, parent, false);
        return new ExpenseHolder(v);
    }

    class ExpenseHolder extends RecyclerView.ViewHolder{
        private TextView mName;
        private TextView mAmount;
        private TextView mMethod;
        private TextView mDescription;
        private TextView mStatus;

        public ExpenseHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.expensePaymentNameTV);
            mAmount = itemView.findViewById(R.id.expensePaymentAmountTV);
            mMethod = itemView.findViewById(R.id.expensePaymentMethodTV);
            mDescription = itemView.findViewById(R.id.expensePaymentDescriptionTV);
            mStatus = itemView.findViewById(R.id.expensePaymentStatusTV);
        }

    }
}
