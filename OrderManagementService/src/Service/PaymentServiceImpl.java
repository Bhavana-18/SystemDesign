package Service;

import Entity.Payment;
import Enums.PaymentMode;
import Enums.PaymentStatus;
import Repository.PaymentRepository;

import java.util.UUID;

public class PaymentServiceImpl implements  PaymentService {

    private final PaymentRepository paymentRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment initiate(String orderId, PaymentMode mode){
        if(orderId == null || orderId.isBlank()){
            throw new IllegalArgumentException("orderId should not be empty");
        }
        if(mode == null ){
            throw new IllegalArgumentException("payment mode cannot be null");
        }
        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, orderId, mode);
        paymentRepository.save(payment);
        return payment;

    }

    @Override
    public void markSuccess(String paymentId, String externalRef){
        if(paymentId == null || paymentId.isBlank()){
            throw new IllegalArgumentException("paymentId cannot be null");
        }
        if(externalRef == null || externalRef.isBlank()){
            throw new IllegalArgumentException("externalRef should not be null");
        }
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found" + paymentId));
        if(payment.getStatus() == PaymentStatus.SUCCESS){
            return;
        }

        if(payment.getStatus() == PaymentStatus.FAILED){
            throw new RuntimeException("Payment already FAILED; cannot mark SUCCESS. paymentId=" + paymentId);
        }

        payment.markSuccess(externalRef);
        paymentRepository.save(payment);
    }
    @Override
    public void markFailed(String paymentId, String reason) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId cannot be null/blank");
        }
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("reason cannot be null/blank");
        }

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));

        // Idempotency:
        if (payment.getStatus() == PaymentStatus.FAILED) {
            return; // already failed
        }
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment already SUCCESS; cannot mark FAILED. paymentId=" + paymentId);
        }

        payment.markFailed(reason);
        paymentRepository.save(payment);
    }

    @Override
    public Payment get(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("paymentId cannot be null/blank");
        }
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
    }



}
