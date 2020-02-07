package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.MedicineDTO;
import ftn.tim16.ClinicalCentreSystem.model.Medicine;

import java.util.List;

public interface MedicineService {

    List<MedicineDTO> findAll();

    Medicine findById(Long id);

    Medicine findByLabel(String label);

    MedicineDTO create(MedicineDTO medicine);
}
