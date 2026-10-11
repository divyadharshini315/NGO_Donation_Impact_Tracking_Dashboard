
package com.ngo.donation.dto;

public class RegisterRequestDTO {

    private String name;
    private String email;
    private String password;
    private String role;
    private String otp;

    // Volunteer profile fields
    private String phone;
    private String address;
    private String skills;
    private String availability;

    // Admin registration field
    private String adminSecretCode;

    public RegisterRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getAdminSecretCode() {
        return adminSecretCode;
    }

    public void setAdminSecretCode(String adminSecretCode) {
        this.adminSecretCode = adminSecretCode;
    }
}