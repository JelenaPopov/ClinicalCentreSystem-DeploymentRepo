package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.model.DateTimeInterval;

import java.time.LocalDateTime;

public interface DateTimeIntervalService {
    public DateTimeInterval create(LocalDateTime startDate, LocalDateTime endDate);
}
