package com.example.Student_Management.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Student {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be 100 characters or fewer")
    private String name;

    @NotBlank(message = "Roll number is required")
    @Size(max = 30, message = "Roll number must be 30 characters or fewer")
    private String rollNo;

    @NotNull(message = "Admission year is required")
    @Min(value = 2000, message = "Enter a valid admission year")
    @Max(value = 2100, message = "Enter a valid admission year")
    private Integer admissionYear;

    @NotNull(message = "12th percentage is required")
    @DecimalMin(value = "0.0", message = "Percentage cannot be less than 0")
    @DecimalMax(value = "100.0", message = "Percentage cannot be more than 100")
    private Float twelfthPercentage;

    @NotNull(message = "EAPCET rank is required")
    @Min(value = 1, message = "Rank must be at least 1")
    private Integer eapcetRank;

    @NotBlank(message = "Branch is required")
    @Size(max = 100, message = "Branch must be 100 characters or fewer")
    private String branch;

    @NotBlank(message = "Status is required")
    @Size(max = 30, message = "Status must be 30 characters or fewer")
    private String status;

    @Pattern(regexp = "^$|[0-9+\\- ]{7,15}$", message = "Enter a valid phone number")
    private String phone;

    @Size(max = 255, message = "Address must be 255 characters or fewer")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Integer getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(Integer admissionYear) {
        this.admissionYear = admissionYear;
    }

    public Float getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(Float twelfthPercentage) {
        this.twelfthPercentage = twelfthPercentage;
    }

    public Integer getEapcetRank() {
        return eapcetRank;
    }

    public void setEapcetRank(Integer eapcetRank) {
        this.eapcetRank = eapcetRank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
    this.branch = (branch == null) ? null : branch.trim().toUpperCase();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
