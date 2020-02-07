package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.enumeration.UserStatus;
import ftn.tim16.ClinicalCentreSystem.model.Nurse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

    Nurse findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
        //@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "0")})
    Nurse findOneById(Long id);

    Nurse findByIdAndStatus(Long id, UserStatus userStatus);

    Nurse findByPhoneNumber(String phoneNumber);

    Nurse findByPhoneNumberAndIdNot(String phoneNumber, Long id);

    List<Nurse> findAllByClinicId(Long id);

    Page<Nurse> findAllByClinicId(Long id, Pageable page);

    List<Nurse> findByClinicId(Long id);
}
