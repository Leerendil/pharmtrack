package org.vsu.catalogservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vsu.catalogservice.dto.manufacturer.ManufacturerCreateRequest;
import org.vsu.catalogservice.entity.Application;
import org.vsu.catalogservice.entity.Outbox;
import org.vsu.catalogservice.entity.enums.ApplicationStatus;
import org.vsu.catalogservice.events.ApplicationEvent;
import org.vsu.catalogservice.repository.ApplicationRepository;
import org.vsu.catalogservice.repository.OutboxRepository;
import org.vsu.catalogservice.utils.exceptions.ApplicationMalfunctionException;
import org.vsu.catalogservice.utils.exceptions.ApplicationNotFoundException;

import java.util.UUID;

import static org.vsu.catalogservice.entity.enums.ApplicationStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void save(UUID applicationToken, ManufacturerCreateRequest createRequest) {
        try {
            Application entity = Application.builder()
                    .id(applicationToken)
                    .name(createRequest.getName())
                    .country(createRequest.getCountry())
                    .companyMail(createRequest.getCompanyMail())
                    .applicantId(createRequest.getApplicantId())
                    .status(ApplicationStatus.PENDING)
                    .build();

            String jsonPayload = objectMapper.writeValueAsString(
                    ApplicationEvent.builder()
                            .name(entity.getName())
                            .country(entity.getCountry())
                            .companyMail(entity.getCompanyMail())
                            .applicantId(entity.getApplicantId())
                            .status(entity.getStatus())
                            .build()
            );

            Outbox outbox = Outbox.builder()
                    .id(applicationToken)
                    .payload(jsonPayload)
                    .build();

            applicationRepository.save(entity);
            outboxRepository.save(outbox);
        } catch (Exception e) {
            throw new ApplicationMalfunctionException(e);
        }
    }

    public String manage(UUID applicationToken, String verdict) {
        Application entity = applicationRepository.findById(applicationToken)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationToken));

        switch (verdict) {
            case "APPROVED" -> entity.setStatus(APPROVED);
            case "REJECTED" -> entity.setStatus(REJECTED);
            default -> entity.setStatus(FAILED);
        }

        entity = applicationRepository.save(entity);

        return "Application has been managed! Current status: " + entity.getStatus();
    }
}
