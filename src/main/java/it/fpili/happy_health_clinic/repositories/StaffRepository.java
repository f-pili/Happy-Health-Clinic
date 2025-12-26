package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Staff;
import it.fpili.happy_health_clinic.enums.StaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for accessing and managing Staff entities.
 * Provides CRUD operations and custom queries for staff member data.
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

    /**
     * Finds all staff members with a specific role.
     *
     * @param staffRole the staff role to filter by
     * @return list of staff members with the specified role
     */
    List<Staff> findByStaffRole(StaffRole staffRole);
}
