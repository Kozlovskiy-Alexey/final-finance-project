package by.itacademy.classifier.controller;

import by.itacademy.classifier.dto.CategoryDto;
import by.itacademy.classifier.dto.CategoryPageDto;
import by.itacademy.classifier.dto.CurrencyDto;
import by.itacademy.classifier.dto.CurrencyPageDto;
import by.itacademy.classifier.service.api.ICategoryService;
import by.itacademy.classifier.service.api.ICurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classifier")
public class ClassifierController {

    private final ICurrencyService currencyService;
    private final ICategoryService categoryService;

    public ClassifierController(ICurrencyService currencyService, ICategoryService categoryService) {
        this.currencyService = currencyService;
        this.categoryService = categoryService;
    }

    @PostMapping("/currency")
    public ResponseEntity<CurrencyDto> addNewCurrency(@RequestBody CurrencyDto currencyDTO) {
        CurrencyDto dto = currencyService.addCurrency(currencyDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/currency")
    public ResponseEntity<CurrencyPageDto> getCurrencyPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                                           @RequestParam(name = "size", defaultValue = "100") int size) {
        CurrencyPageDto dto = currencyService.getCurrencyPage(page, size);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/operation/category")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto dto = categoryService.add(categoryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/operation/category")
    public ResponseEntity<CategoryPageDto> getCategoryPage(@RequestParam(name = "page", defaultValue = "1") int page,
                                                           @RequestParam(name = "size", defaultValue = "100") int size) {
        CategoryPageDto dto = categoryService.getCategoryPage(page, size);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
