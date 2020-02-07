package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.ClinicDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.EditClinicDTO;
import ftn.tim16.ClinicalCentreSystem.model.Clinic;
import ftn.tim16.ClinicalCentreSystem.model.Examination;
import ftn.tim16.ClinicalCentreSystem.repository.ClinicRepository;
import ftn.tim16.ClinicalCentreSystem.service.ClinicService;
import ftn.tim16.ClinicalCentreSystem.service.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private ExaminationService examinationService;

    @Override
    public Clinic findOneById(Long id) {
        return clinicRepository.findOneById(id);
    }

    @Override
    public ClinicDTO findById(Long id) {
        Clinic clinic = clinicRepository.findOneById(id);
        if (clinic == null) {
            return null;
        }

        return new ClinicDTO(clinic);
    }

    @Override
    public Clinic findByName(String name) {
        return clinicRepository.findByNameIgnoringCase(name);
    }

    @Override
    public Clinic findByAddress(String address) {
        return clinicRepository.findByAddressIgnoringCase(address);
    }

    @Override
    @Transactional(readOnly = false)
    public ClinicDTO create(ClinicDTO clinicDTO) {
        if (findByName(clinicDTO.getName()) != null || findByAddress(clinicDTO.getAddress()) != null) {
            return null;
        }

        Clinic clinic = new Clinic(clinicDTO.getName(), clinicDTO.getDescription(), clinicDTO.getAddress());

        return new ClinicDTO(clinicRepository.save(clinic));
    }

    @Override
    public List<ClinicDTO> findAll() {
        return convertToDTO(clinicRepository.findAll());
    }

    @Override
    public Integer getClinicRevenue(Long id, String startDateTime, String endDateTime) {
        LocalDateTime startDate = LocalDateTime.of(getDate(startDateTime.toString()), LocalTime.of(0, 0));

        LocalDateTime endDate = LocalDateTime.of(getDate(endDateTime.toString()), LocalTime.of(0, 0, 0));
        if (startDate.isAfter(endDate) || startDate.isAfter(LocalDateTime.now())) {
            return null;
        }

        List<Examination> examinations = examinationService.getClinicExaminations(id, startDate, endDate);
        Double revenue = 0.0;
        for (Examination examination : examinations) {
            if (examination.getDiscount() == null || examination.getDiscount() <= 0) {
                revenue += examination.getExaminationType().getPrice();
            } else {
                revenue += (examination.getExaminationType().getPrice() * (100 - examination.getDiscount()) / 100);
            }
        }
        return revenue.intValue();
    }

    @Override
    public int[] getDailyStatistic(Long clinicId) {
        List<Examination> examinations = examinationService.getAllHeldExaminations(clinicId);

        List<Examination> onMonday = getDailyStatistic(examinations, 1);

        List<Examination> onTue = getDailyStatistic(examinations, 2);

        List<Examination> onWen = getDailyStatistic(examinations, 3);

        List<Examination> onThu = getDailyStatistic(examinations, 4);

        List<Examination> onFri = getDailyStatistic(examinations, 5);

        List<Examination> onSat = getDailyStatistic(examinations, 6);

        List<Examination> onSun = getDailyStatistic(examinations, 7);

        int[] statistic = {onMonday.size(), onTue.size(), onWen.size(), onThu.size(), onFri.size(), onSat.size(), onSun.size()};
        return statistic;
    }


    private List<Examination> getDailyStatistic(List<Examination> examinations, int value) {
        return examinations.stream().filter(p -> p.getInterval().getStartDateTime().getDayOfWeek().getValue() == value).
                collect(Collectors.toCollection(() -> new ArrayList<Examination>()));

    }

    private List<Examination> getWeekStatistic(List<Examination> examinations, int minVal, int maxVal) {
        return examinations.stream().filter(p -> {
            return p.getInterval().getStartDateTime().getDayOfMonth() >= minVal &&
                    p.getInterval().getStartDateTime().getDayOfMonth() <= maxVal;
        }).
                collect(Collectors.toCollection(() -> new ArrayList<Examination>()));
    }

    @Override
    public int[] getWeekStatistic(Long clinicId) {
        List<Examination> examinations = examinationService.getAllHeldExaminations(clinicId);

        List<Examination> first = getWeekStatistic(examinations, 1, 7);

        List<Examination> sec = getWeekStatistic(examinations, 8, 14);

        List<Examination> third = getWeekStatistic(examinations, 15, 21);

        List<Examination> fourth = getWeekStatistic(examinations, 22, 28);

        int[] statistic = {first.size(), sec.size(), third.size(), fourth.size()};
        return statistic;
    }

    @Override
    public int[] getMonthStatistic(Long clinicId) {
        List<Examination> examinations = examinationService.getAllHeldExaminations(clinicId);

        List<Examination> jan = getMountExamination(examinations, 1);

        List<Examination> feb = getMountExamination(examinations, 2);

        List<Examination> march = getMountExamination(examinations, 3);

        List<Examination> april = getMountExamination(examinations, 4);

        List<Examination> may = getMountExamination(examinations, 5);

        List<Examination> june = getMountExamination(examinations, 6);

        List<Examination> july = getMountExamination(examinations, 7);

        List<Examination> aug = getMountExamination(examinations, 8);

        List<Examination> sep = getMountExamination(examinations, 9);

        List<Examination> october = getMountExamination(examinations, 10);

        List<Examination> nov = getMountExamination(examinations, 11);

        List<Examination> dec = getMountExamination(examinations, 12);

        int[] statistic = {jan.size(), feb.size(), march.size(), april.size(), may.size(), june.size(), july.size(), aug.size(), sep.size(), october.size(), nov.size(), dec.size()};
        return statistic;
    }

    private List<Examination> getMountExamination(List<Examination> examinations, int value) {
        return examinations.stream().filter(p -> p.getInterval().getStartDateTime().getMonthValue() == value).
                collect(Collectors.toCollection(() -> new ArrayList<Examination>()));
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date.substring(0, 10), formatter);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public EditClinicDTO edit(EditClinicDTO clinicDTO, Long clinicIdInWhichAdminWorks) throws Exception {

        Clinic existingClinic = clinicRepository.findOneById(clinicDTO.getId());

        if (existingClinic == null || existingClinic.getId() != clinicIdInWhichAdminWorks) {
            return null;
        }

        Clinic clinicWithSameName = findByName(clinicDTO.getName());
        Clinic clinicWithSameAddress = findByAddress(clinicDTO.getAddress());
        if ((clinicWithSameName != null && clinicWithSameName.getId() != existingClinic.getId())
                || (clinicWithSameAddress != null && clinicWithSameAddress.getId() != existingClinic.getId())) {

            return null;
        }
        existingClinic.setName(clinicDTO.getName());
        existingClinic.setAddress(clinicDTO.getAddress());
        existingClinic.setDescription(clinicDTO.getDescription());
        return new EditClinicDTO(clinicRepository.save(existingClinic));
    }

    private List<ClinicDTO> convertToDTO(List<Clinic> clinics) {
        if (clinics == null || clinics.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClinicDTO> roomDTOS = new ArrayList<>();
        for (Clinic room : clinics) {
            roomDTOS.add(new ClinicDTO(room));
        }
        return roomDTOS;
    }
}
