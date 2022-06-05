package by.itacademy.account.controller;

import by.itacademy.account.dto.AccountDto;
import by.itacademy.account.dto.AccountPageDto;
import by.itacademy.account.service.api.IAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto account) {
        AccountDto accountDto = accountService.add(account);
        return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<AccountPageDto> getPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                                  @RequestParam(name = "size", defaultValue = "100") int size) {

        AccountPageDto accountPageDto = accountService.getPage(page, size);
        return new ResponseEntity<AccountPageDto>(accountPageDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable("uuid") String uuid) {
        AccountDto accountDto = accountService.get(uuid);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }

    @PostMapping(value = "/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<AccountDto> editAccount(@PathVariable("uuid") String uuid,
                                                  @PathVariable("dt_update") long dtUpdate,
                                                  @RequestBody AccountDto accountDTO) {
        AccountDto accountDto = accountService.update(uuid, dtUpdate, accountDTO);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);
    }
}
