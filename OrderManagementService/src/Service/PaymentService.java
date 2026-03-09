package Service;

import Entity.Payment;
import Enums.PaymentMode;

public interface PaymentService {
    Payment initiate(String orderId, PaymentMode paymentMode);
    void markSuccess(String paymentId, String externalRef);
    void markFailed(String paymentId, String reason);
    Payment get(String paymentId);
 }
