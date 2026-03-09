package Entity;

import Enums.PaymentMode;
import Enums.PaymentStatus;

import java.time.Instant;
import java.util.Objects;

public class Payment {
   private final String paymentId;
   private final String orderId;
   private final PaymentMode paymentMode;
   private PaymentStatus status;

   private String externalRef; //set on success;
    private  String failureReason;

    private final Instant createdAt;
    private Instant updatedAt;

    public Payment(String paymentId, String orderId, PaymentMode paymentMode){
        this.paymentId = Objects.requireNonNull(paymentId);
        this.orderId = Objects.requireNonNull(orderId);
        this.paymentMode= Objects.requireNonNull(paymentMode);
        this.status =  PaymentStatus.INITIATED;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public String getPaymentId() {
        return paymentId;
    }
    public String getOrderId() { return orderId; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public PaymentStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getExternalRef() { return externalRef; }
    public String getFailureReason() { return failureReason; }

    public void markSuccess(String externalRef) {
        this.status = PaymentStatus.SUCCESS;
        this.externalRef = externalRef;
        this.failureReason = null;
        this.updatedAt = Instant.now();
    }

    // Call only under orderLock
    public void markFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.externalRef = null;
        this.updatedAt = Instant.now();
    }

    public void markSucess(String externalRef){
        this.status = PaymentStatus.SUCCESS;
        this.externalRef = externalRef;
        this.updatedAt = Instant.now();
        this.failureReason = failureReason;
    }




}
