package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationOrOperationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.PredefinedExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationPagingDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ExaminationService {

    List<Examination> getExaminations(Long idRoom);

    List<Examination> getExaminationsOnDay(Long idRoom, LocalDateTime day);

    List<Examination> getDoctorExaminations(Long idDoctor);

    List<Examination> getDoctorExaminationsOnDay(Long idDoctor, LocalDateTime day);

    List<Examination> getNurseExaminations(Long idNurse);

    List<Examination> getNurseExaminationsOnDay(Long idNurse, LocalDateTime day);

    Examination getExamination(Long id);

    ExaminationPagingDTO getAwaitingExaminations(String kind, ClinicAdministrator clinicAdministrator, Pageable page);

    List<Examination> getAwaitingExaminations();

    ExaminationPagingDTO getPredefinedExaminations(ClinicAdministrator clinicAdministrator, Pageable page);

    Examination assignRoom(Examination examination, Room room, Nurse chosenNurse);

    Examination assignRoomForOperation(Examination selectedExamination, Room room, Set<Doctor> doctors);

    ExaminationPagingDTO getDoctorExaminations(Doctor doctor, Pageable page);

    ExaminationDTO cancelExamination(Doctor doctor, Long examinationId);

    ExaminationDTO createPredefinedExamination(PredefinedExaminationDTO predefinedExaminationDTO, ClinicAdministrator clinicAdministrator);

    List<Examination> getDoctorUpcomingExaminations(Long doctorId);

    List<Examination> getNurseUpcomingExaminations(Long doctorId);

    List<Examination> getUpcomingExaminationsInRoom(Long roomId);

    List<Examination> getUpcomingExaminationsOfExaminationType(Long examinationTypeId);

    Examination getOngoingExamination(Long patientId, Long doctorId, LocalDateTime examinationStartTime);

    List<Examination> getClinicExaminations(Long clinicId, LocalDateTime startDate, LocalDateTime endDateTime);

    List<Examination> getAllHeldExaminations(Long clinicId);

    ExaminationDTO createExaminationOrOperation(CreateExaminationOrOperationDTO createExaminationOrOperationDTO, Doctor loggedDoctor);

    List<Examination> getExaminationsAfter(Long idRoom, LocalDateTime endDateTime);

    List<Examination> getDoctorExaminationsBetween(Long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Examination> getNurseExaminationsBetween(Long nurseId, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean hasDoctorHeldExaminationForPatient(Doctor doctor, Patient patient);

    boolean hasNurseHeldExaminationForPatient(Nurse nurse, Patient patient);
}
