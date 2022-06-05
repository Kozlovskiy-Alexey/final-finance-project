package by.itacademy.account.scheduler.controller;

import by.itacademy.account.scheduler.dto.ScheduledOperationDto;
import by.itacademy.account.scheduler.dto.ScheduledOperationPageDto;
import by.itacademy.account.scheduler.service.SchedulerService;
import by.itacademy.account.scheduler.service.ScheduledOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scheduler/operation")
public class SchedulerController {

    private final ScheduledOperationService scheduledOperationService;
    private final SchedulerService schedulerService;

    public SchedulerController(ScheduledOperationService scheduledOperationService, SchedulerService schedulerService) {
        this.scheduledOperationService = scheduledOperationService;
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<ScheduledOperationDto> addScheduleOperation(@RequestBody ScheduledOperationDto scheduledOperationDto) {
        ScheduledOperationDto dto = scheduledOperationService.add(scheduledOperationDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ScheduledOperationPageDto> getSchedulerOperationPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "100") int size) {
        ScheduledOperationPageDto pageDto = scheduledOperationService.get(page, size);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }

    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<ScheduledOperationDto> update(@PathVariable("uuid") String uuid,
                                                        @PathVariable(value = "dt_update", required = false) long dtUpdate,
                                                        @RequestBody ScheduledOperationDto scheduledOperationDto) {
        ScheduledOperationDto dto = scheduledOperationService.update(uuid, dtUpdate, scheduledOperationDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
