package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {

    List<Diagnose> findAll();

    Diagnose findOneById(Long id);

    Diagnose findByTitleIgnoringCase(String title);
}
