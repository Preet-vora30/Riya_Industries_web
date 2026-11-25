package com.riyaindustries.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnquiryFormDto {

    @NotBlank(message = "Please enter your name")
    @Size(max = 150)
    private String name;

    @Size(max = 180)
    private String companyName;

    @NotBlank(message = "Please enter your phone number")
    @Size(max = 40)
    private String phone;

    @Email(message = "Please enter a valid email address")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Please enter a subject")
    @Size(max = 200)
    private String subject;

    @NotBlank(message = "Please enter your enquiry message")
    @Size(max = 3000)
    private String message;

    private String sourcePage = "website";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }
}
