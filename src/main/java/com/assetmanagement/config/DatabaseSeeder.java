package com.assetmanagement.config;

import com.assetmanagement.model.*;
import com.assetmanagement.repository.RenewalHistoryRepository;
import com.assetmanagement.repository.SoftwareLicenseRepository;
import com.assetmanagement.repository.TeamRepository;
import com.assetmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final SoftwareLicenseRepository licenseRepository;
    private final RenewalHistoryRepository renewalHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository, TeamRepository teamRepository,
                          SoftwareLicenseRepository licenseRepository, RenewalHistoryRepository renewalHistoryRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.licenseRepository = licenseRepository;
        this.renewalHistoryRepository = renewalHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial seeding.");
            return;
        }

        log.info("Seeding initial teams, users, licenses, and renewal histories...");

        // 1. Teams
        Team engineering = teamRepository.save(new Team("Engineering", "ENG", "Software Development & Architecture"));
        Team marketing = teamRepository.save(new Team("Marketing", "MKT", "Product Marketing & Growth"));
        Team itOps = teamRepository.save(new Team("IT Operations", "ITO", "Infrastructure, Security & Support"));

        // 2. Users (Default password: password123)
        User admin = userRepository.save(new User(
                "admin", "admin@company.com", passwordEncoder.encode("password123"), Role.ROLE_ADMIN, itOps));

        User managerEng = userRepository.save(new User(
                "manager_eng", "manager.eng@company.com", passwordEncoder.encode("password123"), Role.ROLE_MANAGER, engineering));

        User employeeDev = userRepository.save(new User(
                "dev_john", "john.dev@company.com", passwordEncoder.encode("password123"), Role.ROLE_EMPLOYEE, engineering));

        User employeeMarketing = userRepository.save(new User(
                "sarah_mkt", "sarah.mkt@company.com", passwordEncoder.encode("password123"), Role.ROLE_EMPLOYEE, marketing));

        // 3. Software Licenses
        LocalDate today = LocalDate.now();

        // Active license
        SoftwareLicense intellij = new SoftwareLicense(
                "IntelliJ IDEA Ultimate", "JetBrains", "JB-ENG-98234-ULT",
                LicenseType.PER_SEAT, 50, new BigDecimal("24950.00"),
                today.minusMonths(6), today.plusMonths(6), engineering
        );
        intellij.setAllocatedSeats(38);
        intellij.setStatus(LicenseStatus.ACTIVE);
        intellij = licenseRepository.save(intellij);

        // Expiring soon license (< 30 days)
        SoftwareLicense figma = new SoftwareLicense(
                "Figma Enterprise", "Figma Inc.", "FIGMA-ORG-77120-ENT",
                LicenseType.SUBSCRIPTION, 30, new BigDecimal("14400.00"),
                today.minusMonths(11), today.plusDays(15), marketing
        );
        figma.setAllocatedSeats(25);
        figma.setStatus(LicenseStatus.EXPIRING_SOON);
        figma = licenseRepository.save(figma);

        // Expired license
        SoftwareLicense postman = new SoftwareLicense(
                "Postman Enterprise", "Postman", "POSTMAN-TEAM-44391",
                LicenseType.PER_SEAT, 20, new BigDecimal("5800.00"),
                today.minusYears(1), today.minusDays(5), engineering
        );
        postman.setAllocatedSeats(19);
        postman.setStatus(LicenseStatus.EXPIRED);
        postman = licenseRepository.save(postman);

        // 4. Renewal History
        RenewalHistory jetbrainsPastRenewal = new RenewalHistory(
                intellij,
                today.minusMonths(18),
                today.minusMonths(6),
                new BigDecimal("22500.00"),
                admin,
                "Annual volume renewal for Engineering department with 5 additional seats."
        );
        jetbrainsPastRenewal.setRenewalTimestamp(LocalDateTime.now().minusMonths(6));
        renewalHistoryRepository.save(jetbrainsPastRenewal);

        log.info("Initial database seeding completed successfully.");
    }
}
