package it.fpili.happy_health_clinic.entities;

import it.fpili.happy_health_clinic.enums.StaffRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity representing a staff member.
 * Extends Employee with staff-specific role and responsibility information.
 */
@Entity
@Table(name = "staff")
@Data
@EqualsAndHashCode(callSuper = true)
public class Staff extends Employee {

    /**
     * The staff member's specific role within the organization.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "staff_role", nullable = false, length = 100)
    private StaffRole staffRole;

    /**
     * A description of the staff member's responsibilities and duties.
     */
    @Column(columnDefinition = "TEXT")
    private String responsibilities;
}
