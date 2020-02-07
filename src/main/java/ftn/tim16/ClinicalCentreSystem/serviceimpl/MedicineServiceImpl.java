package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.MedicineDTO;
import ftn.tim16.ClinicalCentreSystem.model.Medicine;
import ftn.tim16.ClinicalCentreSystem.repository.MedicineRepository;
import ftn.tim16.ClinicalCentreSystem.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public List<MedicineDTO> findAll() {
        return convertToDTO(medicineRepository.findAll());
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findOneById(id);
    }

    @Override
    public Medicine findByLabel(String label) {
        return medicineRepository.findByLabelIgnoringCase(label);
    }

    @Override
    @Transactional(readOnly = false)
    public MedicineDTO create(MedicineDTO medicine) {
        Medicine medicineWithSameLabel = findByLabel(medicine.getLabel());
        if (medicineWithSameLabel != null) {
            return null;
        }
        Medicine newMedicine = new Medicine(medicine.getLabel(), medicine.getChemicalComposition(), medicine.getUsage());
        return new MedicineDTO(medicineRepository.save(newMedicine));
    }

    private List<MedicineDTO> convertToDTO(List<Medicine> medicines) {
        if (medicines == null || medicines.isEmpty()) {
            return new ArrayList<>();
        }
        List<MedicineDTO> medicineDTOArrayList = new ArrayList<>();
        for (Medicine medicine : medicines) {
            medicineDTOArrayList.add(new MedicineDTO(medicine));
        }
        return medicineDTOArrayList;
    }
}
