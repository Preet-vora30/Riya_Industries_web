package com.riyaindustries.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class QuotationFormDto {

    @NotBlank(message = "Please enter your name")
    @Size(max = 150)
    private String customerName;

    @Size(max = 180)
    private String companyName;

    @NotBlank(message = "Please enter your phone number")
    @Size(max = 40)
    private String phone;

    @Email(message = "Please enter a valid email address")
    @Size(max = 150)
    private String email;

    private Long serviceId;

    @NotBlank(message = "Please specify the required service")
    @Size(max = 150)
    private String requiredServiceName;

    @NotBlank(message = "Please describe the component or job work required")
    @Size(max = 3000)
    private String componentDescription;

    @Size(max = 80)
    private String quantity;

    @Size(max = 120)
    private String material;

    @Size(max = 2000)
    private String technicalRequirements;

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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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
}
