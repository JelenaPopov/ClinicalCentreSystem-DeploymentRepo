package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.TimeOffStatus;
import ftn.tim16.ClinicalCentreSystem.model.TimeOffDoctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeOffDoctorRepository extends JpaRepository<TimeOffDoctor, Long> {

    List<TimeOffDoctor> findByDoctorIdAndStatus(Long id, TimeOffStatus status);

    List<TimeOffDoctor> findByDoctorIdAndStatusNot(Long id, TimeOffStatus status);

    List<TimeOffDoctor> findByDoctorClinicIdAndStatus(Long id, TimeOffStatus status);

    TimeOffDoctor findByIdAndStatus(Long id, TimeOffStatus status);

}
