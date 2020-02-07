package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.DoctorStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {

    List<Examination> findByRoomIdAndStatusNotOrderByIntervalStartDateTime(Long id, ExaminationStatus status);

    List<Examination> findByRoomIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
            (Long id, ExaminationStatus status, LocalDateTime greater, LocalDateTime less);

    List<Examination> findByDoctorsIdAndStatusNot(Long id, ExaminationStatus status);

    List<Examination> findByDoctorsIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
            (Long id, ExaminationStatus status, LocalDateTime greater, LocalDateTime less);

    List<Examination> findByNurseIdAndStatusNot(Long id, ExaminationStatus status);

    List<Examination> findByNurseIdAndStatusNotAndIntervalStartDateTimeGreaterThanEqualAndIntervalStartDateTimeLessThan
            (Long id, ExaminationStatus status, LocalDateTime greater, LocalDateTime less);

    Examination getByIdAndStatusNot(Long id, ExaminationStatus status);

    List<Examination> findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter(Long id, ExaminationStatus status, ExaminationKind kind, LocalDateTime now);

    Page<Examination> findByClinicAdministratorIdAndStatusAndKindAndIntervalStartDateTimeAfter(Long id, ExaminationStatus status, ExaminationKind kind, LocalDateTime now, Pageable page);

    Page<Examination> findByClinicAdministratorIdAndStatusIn(Long id, Collection<ExaminationStatus> examinationStatus, Pageable page);

    List<Examination> findByClinicAdministratorIdAndStatusIn(Long id, Collection<ExaminationStatus> examinationStatus);

    List<Examination> findByStatus(ExaminationStatus status);

    List<Examination> findByDoctorsIdAndStatusNotAndIntervalStartDateTimeAfter(Long id, ExaminationStatus status, LocalDateTime localDateTime);

    Page<Examination> findByDoctorsIdAndStatusNotAndIntervalStartDateTimeAfter(Long id, ExaminationStatus status, LocalDateTime localDateTime, Pageable page);

    List<Examination> findByDoctorsIdAndStatusNotAndIntervalEndDateTimeAfter(Long id, ExaminationStatus examinationStatus, LocalDateTime localDateTime);

    Examination findByPatientIdAndDoctorsIdAndDoctorsStatusAndIntervalStartDateTimeLessThanEqualAndIntervalEndDateTimeGreaterThanAndStatusIn(
            Long idP1, Long idD1, DoctorStatus status, LocalDateTime localDateTime, LocalDateTime localDateTimeS, Collection<ExaminationStatus> examinationStatus
    );

    List<Examination> findByNurseIdAndStatusNotAndIntervalEndDateTimeAfter(Long id, ExaminationStatus examinationStatus, LocalDateTime localDateTime);

    List<Examination> findByRoomIdAndStatusNotAndIntervalEndDateTimeAfter(Long id, ExaminationStatus examinationStatus, LocalDateTime localDateTime);

    List<Examination> findByExaminationTypeIdAndStatusNotAndIntervalEndDateTimeAfter(Long id, ExaminationStatus examinationStatus, LocalDateTime localDateTime);

    List<Examination> findByClinicIdAndStatusInAndIntervalStartDateTimeGreaterThanEqualAndIntervalEndDateTimeLessThan(Long id, Collection<ExaminationStatus> examinationStatus,
                                                                                                                      LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Examination> findByClinicIdAndStatusInAndIntervalEndDateTimeLessThan(Long id, Collection<ExaminationStatus> examinationStatus, LocalDateTime endDateTime);

    List<Examination> findByRoomIdAndStatusNotAndIntervalEndDateTimeGreaterThanEqualOrderByIntervalStartDateTime(Long id, ExaminationStatus status, LocalDateTime endDateTime);

    List<Examination> findByClinicIdAndStatusInAndDoctorsIdAndPatientIdAndIntervalStartDateTimeLessThanEqual(Long clinicId, Collection<ExaminationStatus> examinationStatus, Long doctorId,
                                                                                                             Long patientId, LocalDateTime startDateTime);

    List<Examination> findByClinicIdAndStatusInAndNurseIdAndPatientIdAndIntervalStartDateTimeLessThanEqual(Long clinicId, Collection<ExaminationStatus> examinationStatus, Long nurseId,
                                                                                                           Long patientId, LocalDateTime startDateTime);
}
