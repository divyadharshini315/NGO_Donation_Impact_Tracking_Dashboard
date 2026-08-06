NGO Donation & Impact Tracking Dashboard
1. Title

NGO Donation & Impact Tracking Dashboard

2. Domain

Non-Profit Organization (NGO) / Charity Management

3. Who is the User? 
4. Admin (NGO Administrator)

Manages donors, campaigns, and donations.
Tracks fund utilization and generates reports.

2. Donor

Registers and logs in.
Makes donations and views donation history.
Tracks how donated funds are utilized.

3. NGO Staff 

Updates campaign progress.
Uploads impact reports, beneficiary details, and project updates.

4. What Problem Are We Solving?

Many NGOs still maintain donation records manually or using spreadsheets, making it difficult to manage donors and provide transparency about how funds are used. Donors often do not receive clear updates on the impact of their contributions, reducing trust and engagement. For example, if someone donates ₹5,000 towards an education campaign, they may never know how the money was utilized or what outcomes it achieved. This dashboard provides a centralized platform where NGOs can manage donations efficiently while giving donors real-time visibility into campaign progress and impact.

5. Proposed Solution

The application will provide a secure web-based dashboard for NGOs to manage donations and campaigns.

* Features:
User Registration & Login
Donor Profile Management
Campaign Creation & Management
Online Donation Recording
Donation History Tracking
Impact Report Uploads
Dashboard with Donation Statistics
Campaign Progress Tracking
Reports for Admin
Email confirmation for successful donations (optional)

6. Core Entities 

* Users
user_id
name
email
password
role

* Donors

donor_id
user_id
phone
address

* Campaigns

campaign_id
campaign_name
description
target_amount
collected_amount
start_date
end_date
status

* Donations

donation_id
donor_id
campaign_id
amount
donation_date
payment_status

* Impact Reports

report_id
campaign_id
title
description
image_url
report_date

7. User Roles & Permissions

Admin
Login securely
Manage users
Create, edit, and delete campaigns
View all donations
Upload impact reports
View analytics and reports
Manage beneficiary details
Donor
Register/Login
View campaigns
Donate to campaigns
View donation history
Track campaign progress
View impact reports

8. Success Criteria

A new user should be able to register within 2 minutes.
A donor should be able to make a donation in under 1 minute.
Admin should be able to create a campaign in less than 2 minutes.
Donors should be able to view donation history instantly.
Campaign progress and impact reports should be updated in real time after admin actions.
The system should securely store donation and user information.

9. Out of Scope

Payment gateway integration (only donation records will be stored)
Mobile application
AI-based donation recommendations
Multi-language support
SMS notifications
Volunteer management
Accounting or tax filing system
Social media integration
Live chat support

10. Chosen Track

Java (Spring Boot)

Backend: Java, Spring Boot
Frontend: HTML, CSS, Bootstrap, JavaScript (or React if required)
Database: MySQL
ORM: Spring Data JPA (Hibernate)
Authentication: Spring Security (JWT or Session-based)
Build Tool: Maven
API Testing: Postman
Version Control: Git & GitHub