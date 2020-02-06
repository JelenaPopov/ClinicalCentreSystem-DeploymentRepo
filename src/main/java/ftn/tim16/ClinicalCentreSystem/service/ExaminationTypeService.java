package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;

import java.util.List;

public interface ExaminationTypeService {

    ExaminationTypeDTO create(CreateExaminationTypeDTO examinationType, Clinic clinic);

    ExaminationTypeDTO edit(ExaminationTypeDTO examinationType, Long clinicId) throws Exception;

    ExaminationTypeDTO editPriceList(ExaminationTypeDTO examinationType, Long clinicId) throws Exception;

    List<ExaminationTypeDTO> findAllTypesInClinic(Long clinicId);

    List<ExaminationTypeDTO> searchTypesInClinic(Clinic clinic, String searchLabel, Double searchPrice);

    ExaminationType findById(Long id);

    ExaminationTypeDTO deleteExaminationType(Long clinicId, Long id) throws Exception;
}
