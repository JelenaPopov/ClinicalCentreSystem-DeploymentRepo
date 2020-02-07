package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;

import java.util.List;

public interface ClinicService {

    Clinic findOneById(Long id);

    ClinicDTO findById(Long id);

    Clinic findByName(String name);

    Clinic findByAddress(String address);

    ClinicDTO create(ClinicDTO clinicDTO);

    List<ClinicDTO> findAll();

    Integer getClinicRevenue(Long id, String startDateTime, String endDateTime);

    int[] getDailyStatistic(Long clinicId);

    int[] getWeekStatistic(Long clinicId);

    int[] getMonthStatistic(Long clinicId);

    EditClinicDTO edit(EditClinicDTO clinicDTO, Long clinicIdInWhichAdminWorks) throws Exception;
}
