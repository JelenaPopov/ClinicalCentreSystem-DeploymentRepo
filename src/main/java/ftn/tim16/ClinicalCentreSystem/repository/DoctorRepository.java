package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Collection;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByIdAndStatusNot(Long id, DoctorStatus doctorStatus);

    Doctor findByIdAndStatusNotIn(Long id, Collection<DoctorStatus> doctorStatuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
        //@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "0")})
    Doctor findByIdAndStatusIn(Long id, Collection<DoctorStatus> doctorStatuses);

    Doctor findByPhoneNumber(String phoneNumber);

    Doctor findByPhoneNumberAndIdNot(String phoneNumber, Long id);

    List<Doctor> findByClinicIdAndStatusNot(Long id, DoctorStatus status);

    List<Doctor> findByClinicIdAndStatusNotAndFirstNameContainsIgnoringCaseAndLastNameContainsIgnoringCaseAndSpecializedLabelContainsIgnoringCase(
            Long id, DoctorStatus status, String firstName, String lastName, String specialized);

    Doctor findByEmail(String email);

    List<Doctor> findByFirstNameContainsIgnoringCaseAndLastNameContainsIgnoringCaseAndDoctorRating(String firstName, String lastName, int doctorRating);

    List<Doctor> findByClinicIdAndSpecializedIdAndStatusNot(Long clinic_id, Long specialized, DoctorStatus status);

    List<Doctor> findByStatusNotAndClinicIdAndSpecializedId(DoctorStatus status, Long clinicId, Long specializedId);

}
