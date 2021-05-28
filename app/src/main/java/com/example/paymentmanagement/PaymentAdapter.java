package com.example.paymentmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    private ArrayList<Payment> mPaymentData;
    private Context mContext;


    PaymentAdapter(Context context, ArrayList<Payment> paymentsData){
        this.mPaymentData = paymentsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.payment_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment currentPayment = mPaymentData.get(position);
        holder.bindTo(currentPayment);
    }

    @Override
    public int getItemCount() {
        return mPaymentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mAmount;
        private TextView mMethod;
        private TextView mDescription;
        private TextView mStatus;
        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.paymentNameTV);
            mAmount = itemView.findViewById(R.id.paymentAmountTV);
            mMethod = itemView.findViewById(R.id.paymentMethodTV);
            mDescription = itemView.findViewById(R.id.paymentDescriptionTV);
            mStatus = itemView.findViewById(R.id.paymentStatusTV);
            view = itemView;
        }

        public void bindTo(Payment currentPayment) {
            mName.setText(currentPayment.getName());
            mAmount.setText(currentPayment.getAmount().toString());
            mMethod.setText(currentPayment.getPaymentMethod());
            mDescription.setText(currentPayment.getDescription());
            mStatus.setText(currentPayment.getStatus());

            view.findViewById(R.id.deleteButton_list).setOnClickListener(v -> {
                FirebaseFirestore.getInstance().collection("Payments").document(currentPayment.getFirebaseId()).delete().addOnSuccessListener(unused -> {
                    mPaymentData.remove(currentPayment);
                    notifyDataSetChanged();
                });
            });

            view.findViewById(R.id.editButton_list).setOnClickListener(v -> {
                Intent intent = new Intent(view.getContext(), EditPaymentActivity.class);
                intent.putExtra("payment", currentPayment);

                ((Activity) view.getContext()).startActivityForResult(intent, 0);
            });
        }
    }
}
