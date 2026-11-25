package com.riyaindustries.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotation_requests")
public class QuotationRequest extends BaseEntity {

    @Column(name = "reference_code", nullable = false, unique = true, length = 40)
    private String referenceCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MachiningService service;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "company_name", length = 180)
    private String companyName;

    @Column(nullable = false, length = 40)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(name = "required_service_name", nullable = false, length = 150)
    private String requiredServiceName;

    @Column(name = "component_description", nullable = false, columnDefinition = "TEXT")
    private String componentDescription;

    @Column(length = 80)
    private String quantity;

    @Column(length = 120)
    private String material;

    @Column(name = "technical_requirements", columnDefinition = "TEXT")
    private String technicalRequirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkStatus status = WorkStatus.NEW;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @OneToMany(mappedBy = "quotationRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuotationAttachment> attachments = new ArrayList<>();

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MachiningService getService() {
        return service;
    }

    public void setService(MachiningService service) {
        this.service = service;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequiredServiceName() {
        return requiredServiceName;
    }

    public void setRequiredServiceName(String requiredServiceName) {
        this.requiredServiceName = requiredServiceName;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getTechnicalRequirements() {
        return technicalRequirements;
    }

    public void setTechnicalRequirements(String technicalRequirements) {
        this.technicalRequirements = technicalRequirements;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public List<QuotationAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<QuotationAttachment> attachments) {
        this.attachments = attachments;
    }
}
