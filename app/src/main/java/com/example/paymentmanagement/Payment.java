package com.example.paymentmanagement;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.type.DateTime;

public class Payment implements Parcelable {
    private String firebaseId;
    private String account; //type of account
    private Money amount;
    private String authorizationCode;
    private String correlatorId;
    private String description;
    private String href;
    private String id;
    private String name; //name of Payment
    private String payer;
    private DateTime paymentDate;
    private PaymentItem[] paymentItem; //list of paymentItems
    private String paymentMethod;
    private String status;
    private DateTime statusDate;
    private String userId;


    public Payment(String name, Money amount, String paymentMethod, String description,  String status, String userId){
        this.name = name;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }
    public Payment(String account, Money amount, String authorizationCode, String correlatorId,
                   String description, String href, String id, String name, String payer,
                   DateTime paymentDate, PaymentItem[] paymentItem, String paymentMethod,
                   String status, DateTime statusDate, String userId){
        this.account = account;
        this.amount = amount;
        this.authorizationCode = authorizationCode;
        this.correlatorId = correlatorId;
        this.description = description;
        this.href = href;
        this.id = id;
        this.name = name;
        this.payer = payer;
        this.paymentDate = paymentDate;
        this.paymentItem = paymentItem;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.statusDate = statusDate;
        this.userId = userId;
    }
    public Payment(){}

    public Payment(Parcel in){
        this.firebaseId = in.readString();
        this.name = in.readString();
        this.amount = in.readParcelable(Money.class.getClassLoader());
        this.paymentMethod = in.readString();
        this.description = in.readString();
        this.status = in.readString();
        this.userId = in.readString();

    }

    public static final Parcelable.Creator<Payment> CREATOR = new Parcelable.Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel in) {
            return new Payment(in);
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
        }
    };


    public String getFirebaseId() {
        return firebaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getCorrelatorId() {
        return correlatorId;
    }

    public void setCorrelatorId(String correlatorId) {
        this.correlatorId = correlatorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public DateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentItem[] getPaymentItem() {
        return paymentItem;
    }

    public void setPaymentItem(PaymentItem[] paymentItem) {
        this.paymentItem = paymentItem;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(DateTime statusDate) {
        this.statusDate = statusDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firebaseId);
        dest.writeString(this.name);
        dest.writeParcelable(this.amount,flags);
        dest.writeString(this.paymentMethod);
        dest.writeString(this.description);
        dest.writeString(this.status);
    }

    @NonNull
    @Override
    public String toString() {
        return "Payment{" +
                "firebaseId='" + firebaseId + '\'' +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", method='" + paymentMethod + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", userId= " + userId + '\'' +
                '}';
    }
}