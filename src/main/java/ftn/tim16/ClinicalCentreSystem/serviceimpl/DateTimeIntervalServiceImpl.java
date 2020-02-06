package ftn.tim16.ClinicalCentreSystem.serviceimpl;

import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;
import ftn.tim16.ClinicalCentreSystem.repository.DateTimeIntervalRepository;
import ftn.tim16.ClinicalCentreSystem.service.DateTimeIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class DateTimeIntervalServiceImpl implements DateTimeIntervalService {

    @Autowired
    private DateTimeIntervalRepository dateTimeIntervalRepository;

    @Override
    @Transactional(readOnly = false)
    public DateTimeInterval create(LocalDateTime startDate, LocalDateTime endDate) {
        if (!startDate.isBefore(endDate)) {
            return null;
        }
        DateTimeInterval dateTimeInterval = new DateTimeInterval();
        dateTimeInterval.setStartDateTime(startDate);
        dateTimeInterval.setEndDateTime(endDate);
        return dateTimeIntervalRepository.save(dateTimeInterval);
    }
}
