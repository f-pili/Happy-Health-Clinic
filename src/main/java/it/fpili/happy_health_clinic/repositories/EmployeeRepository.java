package it.fpili.happy_health_clinic.repositories;

import it.fpili.happy_health_clinic.entities.Employee;
import it.fpili.happy_health_clinic.enums.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for accessing and managing Employee entities.
 * Provides CRUD operations and custom queries for employee data.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    /**
     * Finds an employee by their employee code.
     *
     * @param employeeCode the employee code to search for
     * @return an Optional containing the employee if found, empty otherwise
     */
    Optional<Employee> findByEmployeeCode(String employeeCode);

    /**
     * Checks whether an employee with the specified code exists.
     *
     * @param employeeCode the employee code to check
     * @return true if an employee with the code exists, false otherwise
     */
    boolean existsByEmployeeCode(String employeeCode);

    /**
     * Finds all employees with a specific employment status.
     *
     * @param status the employment status to filter by
     * @return list of employees with the specified status
     */
    List<Employee> findByEmployeeStatus(EmploymentStatus status);

    /**
     * Finds all employees in a specific department.
     *
     * @param department the department to filter by
     * @return list of employees in the specified department
     */
    List<Employee> findByDepartment(String department);
}