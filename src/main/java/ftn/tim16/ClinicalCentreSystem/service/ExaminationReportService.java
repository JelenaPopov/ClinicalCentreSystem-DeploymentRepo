package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ExaminationReportDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ExaminationReportForTableDTO;
import ftn.tim16.ClinicalCentreSystem.model.Doctor;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import ftn.tim16.ClinicalCentreSystem.model.ExaminationReport;

import java.util.List;

public interface ExaminationReportService {

    ExaminationReport findByExaminationId(Long examinationId);

    ExaminationReportDTO create(Doctor doctor, Examination examination, ExaminationReportDTO examinationReportDTO);

    ExaminationReportDTO edit(Doctor doctor, ExaminationReportDTO examinationReportDTO);

    List<ExaminationReportForTableDTO> getPatientExaminationReports(Long patientId);

}
