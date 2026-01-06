package com.project.hms.service;

import com.project.hms.entity.*;
import com.project.hms.impl.CashierServiceImpl;
import com.project.hms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CashierService implements CashierServiceImpl {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private CashierRepository cashierRepo;
    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private BillItemRepository billItemRepo;
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private RefundRepository refundRepo;

    @Autowired
    private PatientRepository patientRepository;

    public CashierService(BillRepository billRepo, CashierRepository cashierRepo, PatientRepository patientRepo,
                          BillItemRepository billItemRepo, PaymentRepository paymentRepo, RefundRepository refundRepo) {
        this.billRepo = billRepo;
        this.cashierRepo = cashierRepo;
        this.patientRepo = patientRepo;
        this.billItemRepo = billItemRepo;
        this.paymentRepo = paymentRepo;
        this.refundRepo = refundRepo;
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public List<Bill> getAllBills() {
        return billRepo.findAll();
    }

    @Transactional
    public Bill generateBill(Long patientId, List<BillItem> items, double taxRate) {
        Patient patient = patientRepo.findById(patientId).orElseThrow();
        Bill bill = new Bill();
        bill.setPatientId(patient);

        BigDecimal subtotal = BigDecimal.ZERO;
        for (BillItem item : items) {
            item.setBill(bill);
            item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            subtotal = subtotal.add(item.getLineTotal());
        }
        bill.setItems(items);
        bill.setSubtotal(subtotal);
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(taxRate));
        bill.setTax(tax);
        bill.setTotal(subtotal.add(tax));
        bill.setPaid(false);
        Bill saved = billRepo.save(bill);

        billItemRepo.saveAll(items);
        return saved;
    }

    public Bill getBill(Long id) { return billRepo.findById(id).orElse(null); }

    public List<Bill> getBillsForPatient(Long patientId) {
        return billRepo.findByPatientId(patientId);
    }

    @Transactional
    public Payment acceptFees(Long billId, BigDecimal amount, String method, String reference) {
        Bill bill = billRepo.findById(billId).orElseThrow();
        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setReference(reference);

        paymentRepo.save(payment);

        BigDecimal paid = bill.getAmountPaid().add(amount);
        if (paid.compareTo(bill.getTotal()) >= 0) bill.setPaid(true);
        else bill.setPaid(false);
        billRepo.save(bill);

        return payment;
    }

    @Transactional
    public Bill updateBill(Bill updated) {
        return billRepo.save(updated);
    }

    @Transactional
    public Refund handleRefund(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepo.findById(paymentId).orElseThrow();
        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setAmount(amount);
        refund.setReason(reason);
        refundRepo.save(refund);
        // Optionally, update bill status if necessary
        return refund;
    }

    @Override
    public List<Bill> getBillsByPatientId(Long patientId) {
        if (patientId == null) return Collections.emptyList();
        return billRepo.findByPatientId(patientId);
    }

    public Cashier findByUsername(String username) {
        return cashierRepo.findByUsername(username);
    }

    public Optional<Patient> getPatientById(Long patientId) {
        return patientRepo.findById(patientId);
    }
}