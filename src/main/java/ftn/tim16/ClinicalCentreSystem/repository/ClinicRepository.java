package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    Clinic findOneById(Long id);

    Clinic findByNameIgnoringCase(String name);

    Clinic findByAddressIgnoringCase(String address);

    List<Clinic> findAll();
}
