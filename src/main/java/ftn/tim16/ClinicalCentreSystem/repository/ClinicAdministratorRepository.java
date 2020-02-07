package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.UserStatus;
import ftn.tim16.ClinicalCentreSystem.model.ClinicAdministrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicAdministratorRepository extends JpaRepository<ClinicAdministrator, Long> {
    List<ClinicAdministrator> findAll();

    ClinicAdministrator getByIdAndStatusNot(Long id, UserStatus status);

    ClinicAdministrator findByEmail(String email);

    ClinicAdministrator findByPhoneNumber(String phoneNumber);

    List<ClinicAdministrator> findByClinicId(Long id);

    List<ClinicAdministrator> findByClinicIdAndStatusNot(Long id, UserStatus status);

    ClinicAdministrator findByPhoneNumberAndIdNot(String phoneNumber, Long id);
}
