package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByLabelIgnoringCase(String label);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Room getByIdAndStatusNot(Long id, LogicalStatus status);

    Room getByIdAndStatus(Long id, LogicalStatus status);

    List<Room> findByClinicIdAndStatus(Long id, LogicalStatus status);

    Page<Room> findByClinicIdAndStatusAndLabelContainsIgnoringCase(Long id, LogicalStatus status, String label, Pageable page);

    List<Room> findByClinicIdAndStatusAndLabelContainsIgnoringCase(Long id, LogicalStatus status, String label);

    Page<Room> findByClinicIdAndStatusAndKind(Long id, LogicalStatus status, ExaminationKind kind, Pageable page);

    List<Room> findByClinicIdAndStatusAndKind(Long id, LogicalStatus status, ExaminationKind kind);

    List<Room> findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind(String label, Long id, LogicalStatus status, ExaminationKind kind);

    Page<Room> findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind(String label, Long id, LogicalStatus status, ExaminationKind kind, Pageable page);

}
