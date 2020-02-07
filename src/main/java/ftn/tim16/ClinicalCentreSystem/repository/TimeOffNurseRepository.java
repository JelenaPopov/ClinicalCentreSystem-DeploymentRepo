package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffNurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeOffNurseRepository extends JpaRepository<TimeOffNurse, Long> {

    List<TimeOffNurse> findByNurseIdAndStatus(Long id, TimeOffStatus status);

    List<TimeOffNurse> findByNurseIdAndStatusNot(Long id, TimeOffStatus status);

    List<TimeOffNurse> findByNurseClinicIdAndStatus(Long id, TimeOffStatus status);

    TimeOffNurse findByIdAndStatus(Long id, TimeOffStatus status);
}
