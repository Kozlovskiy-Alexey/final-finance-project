package by.itacademy.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"number", "size", "total_pages", "total_elements", "first",
        "number_of_elements", "last", "content"})
public class AccountPageDto {
    private int number;

    private int size;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_elements")
    private int totalElements;

    private boolean first;

    @JsonProperty("number_of_elements")
    private int numberOfElements;

    private boolean last;

    private List<AccountDto> content;

}