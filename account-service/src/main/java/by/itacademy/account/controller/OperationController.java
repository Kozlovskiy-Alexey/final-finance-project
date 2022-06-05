package by.itacademy.account.controller;

import by.itacademy.account.dto.OperationDto;
import by.itacademy.account.dto.OperationPageDto;
import by.itacademy.account.service.api.IOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account/{uuid}/operation")
public class OperationController {

    private final IOperationService operationService;

    public OperationController(IOperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping
    public ResponseEntity<OperationDto> createOperation(@PathVariable(name = "uuid") String accountUuid,
                                                        @RequestBody OperationDto operationDTO) {
        OperationDto dto = operationService.add(accountUuid, operationDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<OperationPageDto> getOperationPage(@PathVariable(name = "uuid") String uuid,
                                                    @RequestParam(name = "page") int page,
                                                    @RequestParam(name = "size") int size) {
        OperationPageDto operationPageDto = operationService.get(uuid, page, size);
        return new ResponseEntity<>(operationPageDto, HttpStatus.OK);
    }

    @PutMapping("/{uuid_operation}/dt_update/{dt_update}")
    public ResponseEntity<OperationDto> updateOperation(@PathVariable(name = "uuid") String uuid,
                                                        @PathVariable(name = "uuid_operation") String operationUuid,
                                                        @PathVariable(name = "dt_update") long dtUpdate,
                                                        @RequestBody OperationDto operationDTO) {
        OperationDto operationDto = operationService.update(uuid, operationUuid, dtUpdate, operationDTO);
        return new ResponseEntity<>(operationDto, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid_operation}/dt_update/{dt_update}")
    public ResponseEntity<?> deleteOperation(@PathVariable(name = "uuid") String uuid,
                                             @PathVariable(name = "uuid_operation") String operationUuid,
                                             @PathVariable(name = "dt_update") long dtUpdate) {
        operationService.delete(uuid, operationUuid, dtUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
