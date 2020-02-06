package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.dto.request.AssignExaminationDTO;
import ftn.tim16.ClinicalCentreSystem.dto.request.CreateRoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.DoctorDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.RoomWithIdDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.RoomPagingDTO;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationKind;
import ftn.tim16.ClinicalCentreSystem.enumeration.ExaminationStatus;
import ftn.tim16.ClinicalCentreSystem.enumeration.LogicalStatus;
import ftn.tim16.ClinicalCentreSystem.model.*;
import ftn.tim16.ClinicalCentreSystem.repository.RoomRepository;
import ftn.tim16.ClinicalCentreSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockTimeoutException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ExaminationService examinationService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private DateTimeIntervalService dateTimeIntervalService;

    @Override
    public Room findById(Long id) throws LockTimeoutException {
        return roomRepository.getByIdAndStatusNot(id, LogicalStatus.DELETED);
    }

    @Override
    @Transactional(readOnly = false)
    public RoomWithIdDTO create(CreateRoomDTO roomDTO, ClinicAdministrator clinicAdministrator) {
        if (roomRepository.findByLabelIgnoringCase(roomDTO.getLabel()) != null) {
            return null;
        }
        ExaminationKind examinationKind = getKind(roomDTO.getKind());
        if (examinationKind == null) {
            return null;
        }
        Room room = new Room(roomDTO.getLabel(), examinationKind, clinicAdministrator.getClinic());
        return new RoomWithIdDTO(roomRepository.save(room));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RoomWithIdDTO edit(RoomWithIdDTO roomDTO, Long clinicId) throws Exception {
        Room existingRoom = roomRepository.getByIdAndStatus(roomDTO.getId(), LogicalStatus.EXISTING);
        if (existingRoom == null) {
            return null;
        }
        Room roomWithSameLabel = roomRepository.findByLabelIgnoringCase(roomDTO.getLabel());
        if (roomWithSameLabel != null && roomWithSameLabel.getId() != existingRoom.getId()) {
            return null;
        }

        if (!isEditable(existingRoom.getId(), existingRoom.getClinic().getId(), clinicId)) {
            return null;
        }
        ExaminationKind examinationKind = getKind(roomDTO.getKind());
        if (examinationKind == null) {
            return null;
        }
        existingRoom.setLabel(roomDTO.getLabel());
        existingRoom.setKind(examinationKind);
        return new RoomWithIdDTO(roomRepository.save(existingRoom));
    }

    @Override
    public List<RoomDTO> findAllRoomsInClinic(Clinic clinic) {
        return convertToDTO(roomRepository.findByClinicIdAndStatus(clinic.getId(), LogicalStatus.EXISTING));
    }

    @Override
    public RoomPagingDTO searchRoomsInClinic(String kind, Clinic clinic, Pageable page, String searchText, String date,
                                             String searchStartTime, String searchEndTime) throws DateTimeParseException {
        String search = searchText;
        ExaminationKind examinationKind = getKind(kind);
        if (examinationKind == null) {
            return new RoomPagingDTO(
                    convertToDTO(roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(clinic.getId(),
                            LogicalStatus.EXISTING, search, page).getContent()),
                    roomRepository.findByClinicIdAndStatusAndLabelContainsIgnoringCase(clinic.getId(),
                            LogicalStatus.EXISTING, search).size());
        }

        boolean dateSearchActive = true;
        if (date.isEmpty() || searchStartTime.isEmpty() || searchEndTime.isEmpty()) {
            dateSearchActive = false;
        }

        if (search == null) {
            search = "";
        }

        List<Room> roomsInClinicAll = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind(search,
                clinic.getId(), LogicalStatus.EXISTING, examinationKind);

        if (!dateSearchActive) {
            Page<Room> roomsInClinicPage = roomRepository.findByLabelContainsIgnoringCaseAndClinicIdAndStatusAndKind(
                    search, clinic.getId(), LogicalStatus.EXISTING, examinationKind, page);
            return new RoomPagingDTO(convertToDTO(roomsInClinicPage.getContent()), roomsInClinicAll.size());
        }

        LocalDate localDate = getDate(date);
        LocalDateTime startDateTime = getLocalDateTime(localDate, searchStartTime);
        LocalDateTime endDateTime = getLocalDateTime(localDate, searchEndTime);

        List<RoomDTO> availableRoom = searchByDateAndTime(roomsInClinicAll, startDateTime, endDateTime);
        if (availableRoom.isEmpty()) {
            availableRoom = getRoomOnAnotherDate(roomsInClinicAll, startDateTime, endDateTime);
        }
        int start = (int) page.getOffset();
        int end = (start + page.getPageSize()) > availableRoom.size() ? availableRoom.size()
                : (start + page.getPageSize());
        Page<RoomDTO> pages = new PageImpl<>(availableRoom.subList(start, end), page, availableRoom.size());
        return new RoomPagingDTO(pages.getContent(), availableRoom.size());
    }

    @Override
    public List<RoomDTO> getAvailableExaminationRooms(Long clinicId, String startDateTime, String endDateTime) {
        List<Room> rooms = roomRepository.findByClinicIdAndStatusAndKind(clinicId, LogicalStatus.EXISTING,
                ExaminationKind.EXAMINATION);
        return searchByDateAndTime(rooms, getLocalDateTime(startDateTime), getLocalDateTime(endDateTime));
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RoomWithIdDTO deleteRoom(Long clinicId, Long roomId) {
        Room room = roomRepository.getByIdAndStatusNot(roomId, LogicalStatus.DELETED);

        if (room == null) {
            return null;
        }
        if (!isEditable(roomId, room.getClinic().getId(), clinicId)) {
            return null;
        }
        room.setStatus(LogicalStatus.DELETED);
        return new RoomWithIdDTO(roomRepository.save(room));
    }

    private boolean isEditable(Long roomId, Long existingRoomClinicId, Long clinicId) {
        if (existingRoomClinicId != clinicId) {
            return false;
        }
        List<Examination> upcomingExaminations = examinationService.getUpcomingExaminationsInRoom(roomId);

        return !(upcomingExaminations != null && !upcomingExaminations.isEmpty());
    }

    private List<RoomDTO> searchByDateAndTime(List<Room> roomsInClinicAll, LocalDateTime startDateTime,
                                              LocalDateTime endDateTime) {
        List<RoomDTO> availableRoom = new ArrayList<>();
        for (Room currentRoom : roomsInClinicAll) {
            if (isAvailable(currentRoom, startDateTime, endDateTime)) {
                RoomDTO roomDTO = new RoomDTO(currentRoom);
                roomDTO.setAvailable(startDateTime);
                availableRoom.add(roomDTO);
            }
        }
        return availableRoom;
    }

    private List<RoomDTO> getRoomOnAnotherDate(List<Room> roomsInClinicAll, LocalDateTime startDateTime,
                                               LocalDateTime endDateTime) {
        List<RoomDTO> available = new ArrayList<>();
        long duration = Duration.between(startDateTime, endDateTime).toMillis() / 1000;
        for (Room currentRoom : roomsInClinicAll) {
            List<Examination> examinations = examinationService.getExaminationsAfter(currentRoom.getId(),
                    startDateTime);
            if (examinations.isEmpty()) {
                LocalDateTime newEndExamination = endDateTime.plusSeconds(duration);
                if (isAvailable(currentRoom, endDateTime, newEndExamination)) {
                    RoomDTO roomDTO = new RoomDTO(currentRoom);
                    roomDTO.setAvailable(endDateTime);
                    available.add(roomDTO);
                }
            } else {
                for (Examination examination : examinations) {
                    LocalDateTime newEndExamination = examination.getInterval().getEndDateTime().plusSeconds(duration);
                    if (isAvailable(currentRoom, examination.getInterval().getEndDateTime(), newEndExamination)) {
                        RoomDTO roomDTO = new RoomDTO(currentRoom);
                        roomDTO.setAvailable(examination.getInterval().getEndDateTime());
                        available.add(roomDTO);
                        break;
                    }
                }
            }

        }
        available.sort(new Comparator<RoomDTO>() {
            @Override
            public int compare(RoomDTO room1, RoomDTO room2) {
                return room1.getAvailable().isAfter(room2.getAvailable()) ? 1 : -1;
            }
        });

        return available;
    }

    private LocalDateTime getLocalDateTime(LocalDate date, String time) throws DateTimeParseException {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(date, localTime);
    }

    private LocalDate getDate(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private LocalDateTime getLocalDateTime(String date) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

    private ExaminationKind getKind(String kind) {
        try {
            return ExaminationKind.valueOf(kind.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private List<RoomDTO> convertToDTO(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return new ArrayList<>();
        }
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room room : rooms) {
            roomDTOS.add(new RoomDTO(room));
        }
        return roomDTOS;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public RoomWithIdDTO assignRoom(AssignExaminationDTO examination, ClinicAdministrator clinicAdministrator) {
        Examination selectedExamination = examinationService.getExamination(examination.getId());

        if (selectedExamination == null
                || selectedExamination.getClinicAdministrator().getId() != clinicAdministrator.getId()) {
            return null;
        }

        RoomDTO roomDTO = new RoomDTO(examination.getRoomId(), examination.getLabel(), examination.getKind(),
                getLocalDateTime(examination.getAvailable()));

        if (selectedExamination.getKind() == ExaminationKind.EXAMINATION) {
            DoctorDTO doctorDTO = null;
            if (examination.getDoctors() != null && !examination.getDoctors().isEmpty()) {
                doctorDTO = examination.getDoctors().get(0);
            }
            Room assignedRoom = assignRoom(examination.getId(), roomDTO, doctorDTO);
            if (assignedRoom == null) {
                return null;
            }
            return new RoomWithIdDTO(assignedRoom);
        } else {
            if (examination.getDoctors().isEmpty()) {
                return null;
            }

            Set<Doctor> doctors = new HashSet<>();
            for (DoctorDTO doctorDTO : examination.getDoctors()) {
                try {
                    doctors.add(doctorService.findById(doctorDTO.getId()));
                } catch (Exception e) {
                    return null;
                }
            }
            if (doctors.isEmpty()) {
                return null;
            }

            Room assignedRoom = assignRoomForOperation(selectedExamination.getId(), roomDTO, doctors);
            if (assignedRoom == null) {
                return null;
            }
            return new RoomWithIdDTO(assignedRoom);
        }
    }

    private Room assignRoom(Long examinationId, RoomDTO roomDTO, DoctorDTO doctorDTO) {
        Examination selectedExamination = examinationService.getExamination(examinationId);
        Room room = null;
        try {
            room = findById(roomDTO.getId());
        } catch (Exception p) {
            return null;
        }

        if (selectedExamination == null || selectedExamination.getStatus() != ExaminationStatus.AWAITING || room == null
                || room.getKind() != selectedExamination.getKind()) {
            return null;
        }

        if (selectedExamination.getInterval().getStartDateTime().isBefore(LocalDateTime.now())) {
            return null;
        }

        long duration = Duration.between(selectedExamination.getInterval().getStartDateTime(),
                selectedExamination.getInterval().getEndDateTime()).toMillis() / 1000;
        if (!isAvailable(room, roomDTO.getAvailable(), roomDTO.getAvailable().plusSeconds(duration))) {
            return null;
        }

        Doctor doctor = null;
        for (Doctor doc : selectedExamination.getDoctors()) {
            doctor = doc;
            break;
        }
        if (doctor == null) {
            return null;
        }
        Nurse chosenNurse = null;
        if (roomDTO.getAvailable().equals(selectedExamination.getInterval().getStartDateTime())) {
            chosenNurse = nurseService.getRandomNurse(selectedExamination.getClinic().getId(),
                    selectedExamination.getInterval().getStartDateTime(),
                    selectedExamination.getInterval().getEndDateTime());
            if (chosenNurse == null) {
                return null;
            }
            examinationService.assignRoom(selectedExamination, room, chosenNurse);
        } else {
            DateTimeInterval dateTimeInterval = dateTimeIntervalService.create(roomDTO.getAvailable(),
                    roomDTO.getAvailable().plusSeconds(duration));
            if (dateTimeInterval != null) {
                chosenNurse = nurseService.getRandomNurse(selectedExamination.getClinic().getId(),
                        dateTimeInterval.getStartDateTime(), dateTimeInterval.getEndDateTime());
                if (chosenNurse == null) {
                    return null;
                }

                if (!doctorService.haveToChangeDoctor(selectedExamination, doctor, dateTimeInterval.getStartDateTime(),
                        dateTimeInterval.getEndDateTime())) {
                    doctorService.removeExamination(selectedExamination, doctor.getEmail());
                    selectedExamination.getDoctors().remove(doctor);
                    if (doctorDTO == null) {
                        Doctor availableDoctor = doctorService.getAvailableDoctor(
                                selectedExamination.getExaminationType(), dateTimeInterval.getStartDateTime(),
                                dateTimeInterval.getEndDateTime(), selectedExamination.getClinic().getId());
                        if (availableDoctor == null) {
                            return null;
                        }
                        try {
                            doctor = doctorService.findById(availableDoctor.getId());
                        } catch (Exception e) {
                            return null;
                        }
                    } else {
                        try {
                            doctor = doctorService.findById(doctorDTO.getId());
                        } catch (Exception e) {
                            return null;
                        }
                    }

                    if (doctor == null || !doctorService.isAvailable(doctor, dateTimeInterval.getStartDateTime(),
                            dateTimeInterval.getEndDateTime())) {
                        return null;
                    }
                    selectedExamination.getDoctors().add(doctor);
                }
                selectedExamination.setInterval(dateTimeInterval);
                try {
                    examinationService.assignRoom(selectedExamination, room, chosenNurse);
                } catch (IllegalTransactionStateException ex) {
                    return null;
                }

            }

        }
        sendMail(selectedExamination, doctor, selectedExamination.getPatient(), chosenNurse);
        return roomRepository.getByIdAndStatus(roomDTO.getId(), LogicalStatus.EXISTING);
    }

    private Room assignRoomForOperation(Long examinationId, RoomDTO roomDTO, Set<Doctor> doctors) {
        Examination selectedExamination = examinationService.getExamination(examinationId);
        Room room = null;
        try {
            room = findById(roomDTO.getId());
        } catch (Exception p) {
            return null;
        }

        if (selectedExamination == null || room == null || room.getKind() != selectedExamination.getKind()) {
            return null;
        }

        if (selectedExamination.getInterval().getStartDateTime().isBefore(LocalDateTime.now())) {
            return null;
        }

        long duration = Duration.between(selectedExamination.getInterval().getStartDateTime(),
                selectedExamination.getInterval().getEndDateTime()).toMillis() / 1000;
        if (!isAvailable(room, roomDTO.getAvailable(), roomDTO.getAvailable().plusSeconds(duration))) {
            return null;
        }

        if (roomDTO.getAvailable().equals(selectedExamination.getInterval().getStartDateTime())) {
            for (Doctor doctor : doctors) {
                if (!doctorService.isAvailable(doctor, selectedExamination.getInterval().getStartDateTime(),
                        selectedExamination.getInterval().getEndDateTime())) {
                    return null;
                }
            }
            examinationService.assignRoomForOperation(selectedExamination, room, doctors);
        } else {
            DateTimeInterval dateTimeInterval = dateTimeIntervalService.create(roomDTO.getAvailable(),
                    roomDTO.getAvailable().plusSeconds(duration));
            if (dateTimeInterval != null) {
                for (Doctor doctor : doctors) {
                    if (!doctorService.isAvailable(doctor, selectedExamination.getInterval().getStartDateTime(),
                            selectedExamination.getInterval().getEndDateTime())) {
                        return null;
                    }
                }
                selectedExamination.setInterval(dateTimeInterval);
                try {
                    examinationService.assignRoomForOperation(selectedExamination, room, doctors);
                } catch (IllegalTransactionStateException ex) {
                    return null;
                }
            }

        }
        sendMailToAll(selectedExamination, doctors, selectedExamination.getPatient());
        return roomRepository.getByIdAndStatus(roomDTO.getId(), LogicalStatus.EXISTING);
    }

    private void sendMail(Examination examination, Doctor doctor, Patient patient, Nurse nurse) {

        if (doctor == null || patient == null || nurse == null) {
            return;
        }
        String subject = "Notice: Room for the examination has been assigned";
        StringBuilder sb = new StringBuilder();
        sb.append("Examination room for the examination has been assigned.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Examination will be held on ");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        sb.append(examination.getInterval().getStartDateTime().format(dateFormatter));
        sb.append(" between ");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        sb.append(examination.getInterval().getStartDateTime().format(timeFormatter));
        sb.append(" and ");
        sb.append(examination.getInterval().getEndDateTime().format(timeFormatter));
        String text = sb.toString();
        emailNotificationService.sendEmail(doctor.getEmail(), subject, text);
        sb.append(". The examination will be held by the doctor ");
        sb.append(doctor.getFirstName());
        sb.append(" ");
        sb.append(doctor.getLastName());
        String textWithDoctor = sb.toString();
        emailNotificationService.sendEmail(patient.getEmail(), subject, textWithDoctor);
        emailNotificationService.sendEmail(nurse.getEmail(), subject, textWithDoctor);
    }

    private void sendMailToAll(Examination examination, Set<Doctor> doctors, Patient patient) {

        if (doctors == null || doctors.isEmpty() || patient == null) {
            return;
        }
        String subject = "Notice: Room for the operation has been assigned";
        StringBuilder sb = new StringBuilder();
        sb.append("Operating room for the operation has been assigned.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Operation will be held on ");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        sb.append(examination.getInterval().getStartDateTime().format(dateFormatter));
        sb.append(" between ");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        sb.append(examination.getInterval().getStartDateTime().format(timeFormatter));
        sb.append(" and ");
        sb.append(examination.getInterval().getEndDateTime().format(timeFormatter));
        String text = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        int counter = 0;
        for (Doctor doctor : doctors) {
            emailNotificationService.sendEmail(doctor.getEmail(), subject, text);
            if (counter != 0) {
                sb2.append(", ");
            }
            sb2.append(doctor.getFirstName());
            sb2.append(" ");
            sb2.append(doctor.getLastName());
            counter++;
        }

        sb.append(". The operation will perform doctors ");
        sb.append(sb2.toString());
        sb.append(".");

        String textWithDoctors = sb.toString();
        emailNotificationService.sendEmail(patient.getEmail(), subject, textWithDoctors);
    }

    @Override
    public boolean isAvailable(Room currentRoom, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Examination> examinations = examinationService.getExaminationsOnDay(currentRoom.getId(), startDateTime);
        if (!examinations.isEmpty()) {
            for (Examination examination : examinations) {
                if (!examination.getInterval().isAvailable(startDateTime, endDateTime)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void automaticallyAssignRoom() {
        List<Examination> examinations = examinationService.getAwaitingExaminations();
        for (Examination examination : examinations) {
            List<Room> allRooms = roomRepository.findByClinicIdAndStatusAndKind(examination.getClinic().getId(),
                    LogicalStatus.EXISTING, examination.getKind());
            List<RoomDTO> availableRoom = searchByDateAndTime(allRooms, examination.getInterval().getStartDateTime(),
                    examination.getInterval().getEndDateTime());
            if (availableRoom.isEmpty()) {
                availableRoom = getRoomOnAnotherDate(allRooms, examination.getInterval().getStartDateTime(),
                        examination.getInterval().getEndDateTime());
            }

            if (!availableRoom.isEmpty()) {
                if (examination.getKind() == ExaminationKind.EXAMINATION) {
                    assignRoom(examination.getId(), availableRoom.get(new Random().nextInt(availableRoom.size())),
                            null);
                } else {
                    Set<Doctor> availableDoctors = doctorService.getAvailableDoctors(examination.getExaminationType(),
                            examination.getInterval().getStartDateTime(), examination.getInterval().getEndDateTime(),
                            examination.getClinic().getId());

                    if (!availableDoctors.isEmpty()) {
                        assignRoomForOperation(examination.getId(),
                                availableRoom.get(new Random().nextInt(availableRoom.size())), availableDoctors);
                    }
                }
            }

        }
    }
}
