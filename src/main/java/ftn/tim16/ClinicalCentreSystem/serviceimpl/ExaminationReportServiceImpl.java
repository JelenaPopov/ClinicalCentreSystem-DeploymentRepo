package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationReportDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationReportForTableDTO;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.ExaminationReportRepository;
import ftn.tim16.ClinicalCentreSystem.service.DiagnoseService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationReportService;
import ftn.tim16.ClinicalCentreSystem.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ExaminationReportServiceImpl implements ExaminationReportService {

    @Autowired
    private ExaminationReportRepository examinationReportRepository;

    @Autowired
    private DiagnoseService diagnoseService;

    @Autowired
    private MedicineService medicineService;

    @Override
    public ExaminationReport findByExaminationId(Long examinationId) {
        return examinationReportRepository.findByExaminationId(examinationId);
    }

    @Override
    @Transactional(readOnly = false)
    public ExaminationReportDTO create(Doctor doctor, Examination examination, ExaminationReportDTO examinationReportDTO) {
        Diagnose diagnose = diagnoseService.findById(examinationReportDTO.getDiagnoseId());
        if (diagnose == null) {
            return null;
        }

        ExaminationReport examinationReport = new ExaminationReport(LocalDateTime.now(),
                examinationReportDTO.getComment(), examination.getPatient().getMedicalRecord(),
                diagnose, doctor, examination);

        Set<Prescription> prescriptions = new HashSet<>();
        if (examinationReportDTO.getMedicineIds() != null) {
            for (Long medicineId : examinationReportDTO.getMedicineIds()) {
                Medicine medicine = medicineService.findById(medicineId);
                if (medicine == null) {
                    return null;
                }
                Prescription prescription = new Prescription(medicine, examinationReport, examination.getNurse());
                prescriptions.add(prescription);
            }
        }
        examinationReport.setPrescriptions(prescriptions);

        ExaminationReport createdExaminationReport = examinationReportRepository.save(examinationReport);

        return new ExaminationReportDTO(createdExaminationReport);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public ExaminationReportDTO edit(Doctor doctor, ExaminationReportDTO examinationReportDTO) {
        Diagnose diagnose = diagnoseService.findById(examinationReportDTO.getDiagnoseId());
        if (diagnose == null) {
            return null;
        }

        ExaminationReport examinationReport = examinationReportRepository.findByIdAndDoctorId(examinationReportDTO.getId(), doctor.getId());
        if (examinationReport == null) {
            return null;
        }

        examinationReport.setComment(examinationReportDTO.getComment());
        examinationReport.setDiagnose(diagnose);
        ExaminationReport editedExaminationReport = examinationReportRepository.save(examinationReport);

        return new ExaminationReportDTO(editedExaminationReport);
    }

    @Override
    public List<ExaminationReportForTableDTO> getPatientExaminationReports(Long patientId) {
        List<ExaminationReport> patientExaminationReports = examinationReportRepository.findByMedicalRecordPatientIdOrderByTimeCreatedDesc(patientId);
        return convertToDTO(patientExaminationReports);
    }

    private List<ExaminationReportForTableDTO> convertToDTO(List<ExaminationReport> examinationReports) {
        if (examinationReports == null || examinationReports.isEmpty()) {
            return new ArrayList<>();
        }
        List<ExaminationReportForTableDTO> examinationReportForTableDTOS = new ArrayList<>();
        for (ExaminationReport examinationReport : examinationReports) {
            examinationReportForTableDTOS.add(new ExaminationReportForTableDTO(examinationReport));
        }
        return examinationReportForTableDTOS;
    }
}
