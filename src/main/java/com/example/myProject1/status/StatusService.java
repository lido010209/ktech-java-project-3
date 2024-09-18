package com.example.myProject1.status;

import com.example.myProject1.service.WebService;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    private final StatusRepo statusRepo;
    private final WebService webService;
    private static final String[] allStatus =
            {"Pending open", "Opened", "Reject", "Confirm reject", "Request close", "Closed",
            "Ordered", "Paid", "Cancel order"};

    public StatusService(StatusRepo statusRepo, WebService webService) {
        this.statusRepo = statusRepo;
        this.webService = webService;
        for (String name : allStatus) {
            if (!statusRepo.existsByName(name)) {
                Status status = Status.builder()
                        .name(name)
                        .build();
                StatusDto.dto(statusRepo.save(status));
            }
        }
    }
}
