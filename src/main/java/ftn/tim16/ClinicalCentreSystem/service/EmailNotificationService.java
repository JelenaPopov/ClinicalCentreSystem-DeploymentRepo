package ftn.tim16.ClinicalCentreSystem.service;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String text);
}
