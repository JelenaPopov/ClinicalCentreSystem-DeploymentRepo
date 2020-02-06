package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DiagnoseDTO;
import ftn.tim16.ClinicalCentreSystem.model.Diagnose;

import java.util.List;

public interface DiagnoseService {

    List<DiagnoseDTO> findAll();

    Diagnose findById(Long id);

    Diagnose findByTitle(String title);

    DiagnoseDTO create(DiagnoseDTO diagnose);

}
