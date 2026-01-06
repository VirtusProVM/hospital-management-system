package com.project.hms.impl;

import com.project.hms.entity.Bill;
import com.project.hms.entity.BillItem;
import com.project.hms.entity.Payment;
import com.project.hms.entity.Refund;

import java.math.BigDecimal;
import java.util.List;

public interface CashierServiceImpl {
    Bill generateBill(Long patientId, List<BillItem> items, double taxRate);
    Bill getBill(Long id);
    List<Bill> getBillsForPatient(Long patientId);
    Payment acceptFees(Long billId, BigDecimal amount, String method, String reference);
    Bill updateBill(Bill bill); // For updating total/paid/status
    Refund handleRefund(Long paymentId, BigDecimal amount, String reason);
    List<Bill> getBillsByPatientId(Long patientId);
}
