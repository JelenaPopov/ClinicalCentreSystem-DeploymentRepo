package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.CreateExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationTypeDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationType;
import ftn.tim16.ClinicalCentreSystem.repository.ExaminationTypeRepository;
import ftn.tim16.ClinicalCentreSystem.service.DoctorService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExaminationTypeServiceImpl implements ExaminationTypeService {

    @Autowired
    private ExaminationTypeRepository examinationTypeRepository;

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private DoctorService doctorService;

    @Override
    @Transactional(readOnly = false)
    public ExaminationTypeDTO create(CreateExaminationTypeDTO examinationTypeDTO, Clinic clinic) {
        if (examinationTypeRepository.findByLabelIgnoringCase(examinationTypeDTO.getLabel()) != null) {
            return null;
        }
        ExaminationType examinationType = new ExaminationType(examinationTypeDTO.getLabel(), examinationTypeDTO.getPrice(),
                clinic, LogicalStatus.EXISTING);
        return new ExaminationTypeDTO(examinationTypeRepository.save(examinationType));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ExaminationTypeDTO edit(ExaminationTypeDTO examinationType, Long clinicId) throws Exception {
        ExaminationType existingExaminationType = findById(examinationType.getId());
        if (existingExaminationType == null) {
            return null;
        }
        ExaminationType examinationTypeWithSameLabel = examinationTypeRepository.findByLabelIgnoringCase(examinationType.getLabel());
        if (examinationTypeWithSameLabel != null && examinationTypeWithSameLabel.getId() != examinationType.getId()) {
            return null;
        }
        if (!isEditable(examinationType.getId(), existingExaminationType.getClinic().getId(), clinicId)) {
            return null;
        }
        existingExaminationType.setLabel(examinationType.getLabel());
        existingExaminationType.setPrice(examinationType.getPrice());
        return new ExaminationTypeDTO(examinationTypeRepository.save(existingExaminationType));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ExaminationTypeDTO editPriceList(ExaminationTypeDTO examinationType, Long clinicId) throws Exception {
        ExaminationType existingExaminationType = findById(examinationType.getId());
        if (existingExaminationType == null) {
            return null;
        }
        List<Examination> upcomingExaminations = examinationService.getUpcomingExaminationsOfExaminationType(examinationType.getId());
        if (upcomingExaminations != null && !upcomingExaminations.isEmpty()) {
            return null;
        }

        existingExaminationType.setPrice(examinationType.getPrice());
        return new ExaminationTypeDTO(examinationTypeRepository.save(existingExaminationType));
    }

    @Override
    public List<ExaminationTypeDTO> findAllTypesInClinic(Long clinicId) {
        return convertToDTO(examinationTypeRepository.findByClinicIdAndStatus(clinicId, LogicalStatus.EXISTING));
    }

    @Override
    public List<ExaminationTypeDTO> searchTypesInClinic(Clinic clinic, String searchLabel, Double searchPrice) {
        if (searchPrice != null) {
            return convertToDTO(examinationTypeRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCaseAndPrice(clinic.getId(), LogicalStatus.EXISTING,
                    searchLabel, searchPrice));
        }
        return convertToDTO(examinationTypeRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(clinic.getId(),
                LogicalStatus.EXISTING, searchLabel));
    }

    @Override
    public ExaminationType findById(Long id) {
        return examinationTypeRepository.findByIdAndStatusNot(id, LogicalStatus.DELETED);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ExaminationTypeDTO deleteExaminationType(Long clinicId, Long examinationTypeId) throws Exception {
        ExaminationType examinationType = findById(examinationTypeId);
        if (examinationType == null) {
            return null;
        }
        if (examinationType.getClinic().getId() != clinicId) {
            return null;
        }

        if (!isEditable(examinationTypeId, examinationType.getClinic().getId(), clinicId)) {
            return null;
        }
        examinationType.setStatus(LogicalStatus.DELETED);

        return new ExaminationTypeDTO(examinationTypeRepository.save(examinationType));
    }

    private boolean isEditable(Long examinationTypeId, Long examinationTypeClinicId, Long clinicId) {
        if (examinationTypeClinicId != clinicId) {
            return false;
        }
        List<Doctor> doctors = doctorService.findDoctorsByClinicIdAndExaminationTypeId(clinicId, examinationTypeId);
        if (doctors != null && !doctors.isEmpty()) {
            return false;
        }

        List<Examination> upcomingExaminations = examinationService.getUpcomingExaminationsOfExaminationType(examinationTypeId);


        return !(upcomingExaminations != null && !upcomingExaminations.isEmpty());
    }

    private List<ExaminationTypeDTO> convertToDTO(List<ExaminationType> examinationTypes) {
        if (examinationTypes == null || examinationTypes.isEmpty()) {
            return new ArrayList<>();
        }
        List<ExaminationTypeDTO> examinationTypeDTOS = new ArrayList<>();
        for (ExaminationType examinationType : examinationTypes) {
            examinationTypeDTOS.add(new ExaminationTypeDTO(examinationType));
        }
        return examinationTypeDTOS;
    }


}
