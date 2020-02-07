package ftn.tim16.ClinicalCentreSystem.repository;

import ftn.tim16.ClinicalCentreSystem.model.Nurse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static ftn.tim16.ClinicalCentreSystem.constants.DoctorConstants.CLINIC_ID;
import static ftn.tim16.ClinicalCentreSystem.constants.NurseConstants.NUMBER_OF_NURSES;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class NurseRepositoryUnitTests {

    @Autowired
    private NurseRepository nurseRepository;

    @Test
    public void testFindByClinicId() {
        List<Nurse> nurses = nurseRepository.findByClinicId(CLINIC_ID);
        assertEquals(NUMBER_OF_NURSES, nurses.size());
        for (Nurse nurse : nurses) {
            assertEquals(CLINIC_ID, nurse.getClinic().getId());
        }
    }
}
