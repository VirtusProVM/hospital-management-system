package com.project.firestorm.controller;

import com.project.firestorm.entity.Appointments;
import com.project.firestorm.entity.Bill;
import com.project.firestorm.entity.BillItem;
import com.project.firestorm.entity.Patient;
import com.project.firestorm.service.CashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cashier")
public class CashierController {

    private final CashierService cashierService;

    @Autowired
    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @GetMapping("/")
    public String cashierHomePage(Model model) {

        List<Patient> patients = cashierService.getAllPatients();
        List<Bill> bills = cashierService.getAllBills();
        model.addAttribute("patients", patients);
        model.addAttribute("bills", bills);
        return "cashier";
    }

    @GetMapping("/bills")
    public String billsPage(Model model) {
        List<Bill> bills = cashierService.getAllBills();
        model.addAttribute("bills", bills);

        return "bill-list";
    }


    @GetMapping("/bill/generate/{patientId}")
    public String showGenerateBillForm(@PathVariable("patientId") Long patientId, Model model) {
        Optional<Patient> patient = cashierService.getPatientById(patientId);
        model.addAttribute("patient", patient.get());
        return "generate-bill";
    }

    @PostMapping("/bill/generate")
    public String generateBill(@RequestParam Long patientId,
                               @RequestParam List<String> description,
                               @RequestParam List<BigDecimal> unitPrice,
                               @RequestParam List<Integer> quantity,
                               @RequestParam double taxRate,
                               Model model) {
        List<BillItem> items = new java.util.ArrayList<>();
        for (int i = 0; i < description.size(); i++) {
            BillItem item = new BillItem();
            item.setDescription(description.get(i));
            item.setUnitPrice(unitPrice.get(i));
            item.setQuantity(quantity.get(i));
            items.add(item);
        }
        Bill bill = cashierService.generateBill(patientId, items, taxRate);
        return "redirect:/cashier/bill/view/" + bill.getId();
    }

    @GetMapping("/bill/view/{id}")
    public String viewBill(@PathVariable Long id, Model model) {
        Bill bill = cashierService.getBill(id);
        model.addAttribute("bill", bill);
        return "view-bill";
    }

    @PostMapping("/bill/{billId}/accept-fees")
    public String acceptFees(@PathVariable Long billId,
                             @RequestParam BigDecimal amount,
                             @RequestParam String method,
                             @RequestParam(required = false) String reference) {
        cashierService.acceptFees(billId, amount, method, reference);
        return "redirect:/cashier/bill/view/" + billId;
    }

    @PostMapping("/bill/{billId}/update")
    public String updateRecord(@PathVariable Long billId,
                               @RequestParam boolean paid) {
        Bill bill = cashierService.getBill(billId);
        bill.setPaid(paid);
        cashierService.updateBill(bill);
        return "redirect:/cashier/bill/view/" + billId;
    }

    @PostMapping("/bill/{billId}/refund")
    public String handleRefund(@PathVariable Long billId,
                               @RequestParam Long paymentId,
                               @RequestParam BigDecimal amount,
                               @RequestParam String reason) {
        cashierService.handleRefund(paymentId, amount, reason);
        return "redirect:/cashier/bill/view/" + billId;
    }

    @GetMapping("/patient/{patientId}/bills")
    public String listBillsByPatient(@PathVariable("patientId") Long patientId, Model model) {
        List<Bill> bills = cashierService.getBillsByPatientId(patientId);
        model.addAttribute("bills", bills);
        model.addAttribute("patientId", patientId);
        return "cashier-bills-by-patient";
    }
}