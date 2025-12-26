package it.fpili.happy_health_clinic.controllers;

import it.fpili.happy_health_clinic.entities.Staff;
import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import it.fpili.happy_health_clinic.enums.Role;
import it.fpili.happy_health_clinic.enums.StaffRole;
import it.fpili.happy_health_clinic.repositories.StaffRepository;
import it.fpili.happy_health_clinic.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * REST controller for initial application setup operations.
 * Handles creation of default administrative users and other initialization tasks.
 */
@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
public class SetupController {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a default administrator account if one does not already exist.
     *
     * @return a message indicating whether the admin was created or already exists
     */
    @PostMapping("/create-admin")
    public String createAdmin() {
        if (userRepository.existsByEmail("admin@clinic.com")) {
            return "Admin already exists!";
        }

        Staff admin = new Staff();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@clinic.com");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setPhoneNumber("+39 070 1234567");
        admin.setDateOfBirth(LocalDate.of(1985, 1, 15));
        admin.setRole(Role.ADMIN);
        admin.setAvatarUrl("https://ui-avatars.com/api/?name=Admin+User&background=DC2626&color=fff&size=200&bold=true&rounded=true");
        admin.setEmergencyContact("+39 070 7654321");
        admin.setEmployeeCode("EMP-0001");
        admin.setHireDate(LocalDate.of(2023, 1, 10));
        admin.setSalary(BigDecimal.valueOf(45000.00));
        admin.setEmployeeStatus(EmploymentStatus.ACTIVE);
        admin.setDepartment("Administration");
        admin.setStaffRole(StaffRole.ADMIN);
        admin.setResponsibilities("System administration");

        staffRepository.save(admin);

        return "Admin created successfully! Please proceed to login on step 1.1 in order to use the application.";
    }
}