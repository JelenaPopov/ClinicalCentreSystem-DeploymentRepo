package ftn.tim16.ClinicalCentreSystem;

import ftn.tim16.ClinicalCentreSystem.controller.*;
import ftn.tim16.ClinicalCentreSystem.repository.*;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TimeOffDoctorRepositoryUnitTests.class, TimeOffNurseRepositoryUnitTests.class,
        TimeOffDoctorServiceUnitTests.class, TimeOffNurseServiceUnitTests.class,
        TimeOffDoctorControllerUnitTests.class, TimeOffNurseControllerUnitTests.class,
        TimeOffDoctorControllerIntegrationTests.class, TimeOffNurseControllerntegrationTests.class,
        PatientRepositoryUnitTests.class, PatientServiceUnitTests.class, PatientControllerUnitTests.class,
        PatientControllerIntegrationTests.class, RoomControllerUnitTests.class,
        RoomControllerIntegrationTests.class, DoctorControllerUnitTests.class,
        DoctorControllerIntegrationTests.class, ExaminationControllerUnitTests.class,
        ExaminationControllerIntegrationTests.class, RoomServiceUnitTests.class, NurseServiceUnitTests.class,
        ExaminationServiceUnitTests.class, DoctorServiceUnitTests.class,
        RoomRepositoryUnitTests.class, DoctorRepositoryUnitTests.class, NurseRepositoryUnitTests.class,
        ExaminationRepositoryUnitTests.class})
public class TestSuite {
}
