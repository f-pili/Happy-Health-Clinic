package it.fpili.happy_health_clinic.services;

import it.fpili.happy_health_clinic.entities.Appointment;
import it.fpili.happy_health_clinic.entities.Invoice;
import it.fpili.happy_health_clinic.entities.Patient;
import it.fpili.happy_health_clinic.entities.Prescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Service for sending email notifications.
 * Uses Mailgun API to send welcome emails, appointment confirmations, prescription notifications, and invoices.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    /**
     * Mailgun API key for authentication.
     */
    @Value("${mailgun.api.key}")
    private String apiKey;

    /**
     * Mailgun domain name.
     */
    @Value("${mailgun.domain}")
    private String domain;

    /**
     * Email address to send from.
     */
    @Value("${mailgun.from.email}")
    private String fromEmail;

    /**
     * Display name for sent emails.
     */
    @Value("${mailgun.from.name}")
    private String fromName;

    /**
     * Base URL for the Mailgun API.
     */
    @Value("${mailgun.base.url}")
    private String baseUrl;

    /**
     * HTTP client for making requests to Mailgun API.
     */
    private final OkHttpClient httpClient = new OkHttpClient();

    /**
     * Sends a welcome email to a newly registered patient.
     *
     * @param patient the patient to send the welcome email to
     */
    public void sendWelcomeEmail(Patient patient) {
        try {
            String subject = "Welcome to Happy Health Clinic!";
            String htmlContent = buildWelcomeEmailTemplate(patient);

            sendEmail(patient.getEmail(), subject, htmlContent);
            log.info("Welcome email sent successfully to: {}", patient.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", patient.getEmail(), e.getMessage());
        }
    }

    /**
     * Sends an appointment confirmation email to the patient.
     *
     * @param appointment the appointment to confirm
     */
    public void sendAppointmentConfirmation(Appointment appointment) {
        try {
            String subject = "Appointment Confirmation - Happy Health Clinic";
            String htmlContent = buildAppointmentConfirmationTemplate(appointment);

            sendEmail(appointment.getPatient().getEmail(), subject, htmlContent);
            log.info("Appointment confirmation email sent to: {}", appointment.getPatient().getEmail());
        } catch (Exception e) {
            log.error("Failed to send appointment confirmation to {}: {}",
                    appointment.getPatient().getEmail(), e.getMessage());
        }
    }

    /**
     * Sends a prescription notification email to the patient.
     *
     * @param prescription the prescription to notify about
     */
    public void sendPrescriptionNotification(Prescription prescription) {
        try {
            String subject = "New Prescription - Happy Health Clinic";
            String htmlContent = buildPrescriptionNotificationTemplate(prescription);

            String patientEmail = prescription.getMedicalRecord().getPatient().getEmail();
            sendEmail(patientEmail, subject, htmlContent);
            log.info("Prescription notification sent to: {}", patientEmail);
        } catch (Exception e) {
            log.error("Failed to send prescription notification: {}", e.getMessage());
        }
    }

    /**
     * Sends an invoice notification email to the patient.
     *
     * @param invoice the invoice to notify about
     */
    public void sendInvoiceNotification(Invoice invoice) {
        try {
            String subject = "Invoice " + invoice.getInvoiceNumber() + " - Happy Health Clinic";
            String htmlContent = buildInvoiceNotificationTemplate(invoice);

            sendEmail(invoice.getPatient().getEmail(), subject, htmlContent);
            log.info("Invoice notification sent to: {}", invoice.getPatient().getEmail());
        } catch (Exception e) {
            log.error("Failed to send invoice notification to {}: {}",
                    invoice.getPatient().getEmail(), e.getMessage());
        }
    }

    /**
     * Sends an email using the Mailgun API.
     *
     * @param to the recipient email address
     * @param subject the email subject
     * @param htmlContent the email body in HTML format
     * @throws Exception if the email sending fails
     */
    private void sendEmail(String to, String subject, String htmlContent) throws Exception {
        String url = baseUrl + "/" + domain + "/messages";

        String credentials = "api:" + apiKey;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", fromName + " <" + fromEmail + ">")
                .addFormDataPart("to", to)
                .addFormDataPart("subject", subject)
                .addFormDataPart("html", htmlContent)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", basicAuth)
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Mailgun API error: " + response.code() + " - " + response.message());
            }
            log.debug("Email sent successfully to: {}", to);
        }
    }

    /**
     * Builds the HTML template for a welcome email.
     *
     * @param patient the patient to personalize the email
     * @return the HTML email content
     */
    private String buildWelcomeEmailTemplate(Patient patient) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #0D8ABC; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
                    .button { background-color: #0D8ABC; color: white; padding: 10px 20px; text-decoration: none; display: inline-block; border-radius: 5px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to Happy Health Clinic!</h1>
                    </div>
                    <div class="content">
                        <p>Dear %s %s,</p>
                        <p>Thank you for registering with Happy Health Clinic. We're delighted to have you as our patient!</p>
                        <p><strong>Your Patient Information:</strong></p>
                        <ul>
                            <li>Patient Code: <strong>%s</strong></li>
                            <li>Email: %s</li>
                            <li>Phone: %s</li>
                        </ul>
                        <p>You can now book appointments with our doctors and access your medical records through our platform.</p>
                        <p>If you have any questions, please don't hesitate to contact us.</p>
                        <p>Best regards,<br>Happy Health Clinic Team</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 Happy Health Clinic. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPatientCode(),
                patient.getEmail(),
                patient.getPhoneNumber()
        );
    }

    /**
     * Builds the HTML template for an appointment confirmation email.
     *
     * @param appointment the appointment to include in the email
     * @return the HTML email content
     */
    private String buildAppointmentConfirmationTemplate(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        String formattedDate = appointment.getAppointmentDateTime().format(formatter);

        String weatherSection = "";
        if (appointment.getWeatherConditions() != null && !appointment.getWeatherConditions().equals("Weather data unavailable")) {
            weatherSection = """
            <div style="background-color: #EFF6FF; padding: 15px; border-radius: 5px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #2563EB;">🌤️ Weather Forecast</h4>
                <p style="margin: 5px 0;"><strong>Conditions:</strong> %s</p>
                <p style="margin: 5px 0;"><strong>Temperature:</strong> %.1f°C</p>
                %s
            </div>
            """.formatted(
                    appointment.getWeatherConditions(),
                    appointment.getTemperature(),
                    appointment.getWeatherAlert() != null
                            ? "<p style='margin: 5px 0; color: #DC2626;'><strong>" + appointment.getWeatherAlert() + "</strong></p>"
                            : ""
            );
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #2563EB; color: white; padding: 20px; text-align: center; }
                .content { padding: 20px; background-color: #f9f9f9; }
                .appointment-box { background-color: white; padding: 15px; border-left: 4px solid #2563EB; margin: 20px 0; }
                .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>✅ Appointment Confirmed</h1>
                </div>
                <div class="content">
                    <p>Dear %s,</p>
                    <p>Your appointment has been successfully scheduled!</p>
                    <div class="appointment-box">
                        <h3>Appointment Details:</h3>
                        <p><strong>Date & Time:</strong> %s</p>
                        <p><strong>Doctor:</strong> Dr. %s</p>
                        <p><strong>Specialization:</strong> %s</p>
                        <p><strong>Duration:</strong> %d minutes</p>
                        <p><strong>Reason:</strong> %s</p>
                    </div>
                    %s
                    <p><strong>Important:</strong> Please arrive 10 minutes early for check-in.</p>
                    <p>If you need to reschedule or cancel, please contact us at least 24 hours in advance.</p>
                    <p>Best regards,<br>Happy Health Clinic Team</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 Happy Health Clinic. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                appointment.getPatient().getFirstName(),
                formattedDate,
                appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName(),
                appointment.getDoctor().getSpecialization(),
                appointment.getDurationMinutes(),
                appointment.getReason(),
                weatherSection
        );
    }

    /**
     * Builds the HTML template for a prescription notification email.
     *
     * @param prescription the prescription to include in the email
     * @return the HTML email content
     */
    private String buildPrescriptionNotificationTemplate(Prescription prescription) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

        String drugInfoSection = "";
        if (prescription.getDrugInfo() != null && !prescription.getDrugInfo().isEmpty()) {
            drugInfoSection = """
            <div style="background-color: #FEF2F2; padding: 15px; border-radius: 5px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #DC2626;">💊 Drug Information</h4>
                <p style="margin: 5px 0;"><strong>About this medication:</strong></p>
                <p style="margin: 5px 0; font-size: 14px;">%s</p>
            </div>
            """.formatted(prescription.getDrugInfo());
        }

        String sideEffectsSection = "";
        if (prescription.getSideEffects() != null && !prescription.getSideEffects().isEmpty()) {
            sideEffectsSection = """
            <div style="background-color: #FFF7ED; padding: 15px; border-radius: 5px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #EA580C;">⚠️ Possible Side Effects</h4>
                <p style="margin: 5px 0; font-size: 14px;">%s</p>
            </div>
            """.formatted(prescription.getSideEffects());
        }

        String contraindicationsSection = "";
        if (prescription.getContraindications() != null && !prescription.getContraindications().isEmpty()) {
            contraindicationsSection = """
            <div style="background-color: #FEF2F2; padding: 15px; border-radius: 5px; margin: 15px 0;">
                <h4 style="margin-top: 0; color: #991B1B;">🚫 Contraindications</h4>
                <p style="margin: 5px 0; font-size: 14px;">%s</p>
            </div>
            """.formatted(prescription.getContraindications());
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .header { background-color: #DC2626; color: white; padding: 20px; text-align: center; }
                .content { padding: 20px; background-color: #f9f9f9; }
                .prescription-box { background-color: white; padding: 15px; border-left: 4px solid #DC2626; margin: 20px 0; }
                .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header">
                    <h1>💊 New Prescription</h1>
                </div>
                <div class="content">
                    <p>Dear %s,</p>
                    <p>A new prescription has been issued for you.</p>
                    <div class="prescription-box">
                        <h3>Prescription Details:</h3>
                        <p><strong>Medication:</strong> %s</p>
                        <p><strong>Dosage:</strong> %s</p>
                        <p><strong>Frequency:</strong> %s</p>
                        <p><strong>Duration:</strong> %d days</p>
                        <p><strong>Start Date:</strong> %s</p>
                        <p><strong>End Date:</strong> %s</p>
                        <p><strong>Instructions:</strong> %s</p>
                    </div>
                    %s
                    %s
                    %s
                    <p><strong>Important:</strong> Please follow the prescribed dosage and instructions carefully.</p>
                    <p>If you experience any side effects, contact your doctor immediately.</p>
                    <p>Best regards,<br>Happy Health Clinic Team</p>
                </div>
                <div class="footer">
                    <p>&copy; 2025 Happy Health Clinic. All rights reserved.</p>
                </div>
            </div>
        </body>
        </html>
        """.formatted(
                prescription.getMedicalRecord().getPatient().getFirstName(),
                prescription.getDrugName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDurationDays(),
                prescription.getStartDate().format(formatter),
                prescription.getEndDate().format(formatter),
                prescription.getInstructions() != null ? prescription.getInstructions() : "Follow doctor's instructions",
                drugInfoSection,
                sideEffectsSection,
                contraindicationsSection
        );
    }

    /**
     * Builds the HTML template for an invoice notification email.
     *
     * @param invoice the invoice to include in the email
     * @return the HTML email content
     */
    private String buildInvoiceNotificationTemplate(Invoice invoice) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #059669; color: white; padding: 20px; text-align: center; }
                    .content { padding: 20px; background-color: #f9f9f9; }
                    .invoice-box { background-color: white; padding: 15px; border-left: 4px solid #059669; margin: 20px 0; }
                    .footer { text-align: center; padding: 20px; font-size: 12px; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🧾 Invoice Notification</h1>
                    </div>
                    <div class="content">
                        <p>Dear %s,</p>
                        <p>A new invoice has been issued for your recent visit.</p>
                        <div class="invoice-box">
                            <h3>Invoice Details:</h3>
                            <p><strong>Invoice Number:</strong> %s</p>
                            <p><strong>Issue Date:</strong> %s</p>
                            <p><strong>Due Date:</strong> %s</p>
                            <p><strong>Amount:</strong> €%.2f</p>
                            <p><strong>Tax:</strong> €%.2f</p>
                            <p><strong>Total:</strong> €%.2f</p>
                            <p><strong>Description:</strong> %s</p>
                            <p><strong>Status:</strong> %s</p>
                        </div>
                        <p>Please ensure payment is made by the due date to avoid any late fees.</p>
                        <p>For payment methods and questions, please contact our billing department.</p>
                        <p>Best regards,<br>Happy Health Clinic Team</p>
                    </div>
                    <div class="footer">
                        <p>&copy; 2025 Happy Health Clinic. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                invoice.getPatient().getFirstName(),
                invoice.getInvoiceNumber(),
                invoice.getIssueDate().format(formatter),
                invoice.getDueDate().format(formatter),
                invoice.getAmount(),
                invoice.getTaxAmount(),
                invoice.getAmount().add(invoice.getTaxAmount()),
                invoice.getDescription(),
                invoice.getStatus()
        );
    }
}
