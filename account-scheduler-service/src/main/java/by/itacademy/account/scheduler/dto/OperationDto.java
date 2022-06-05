package by.itacademy.account.scheduler.dto;

import by.itacademy.account.scheduler.dto.api.IDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"account", "description", "value", "currency", "category"})
public class OperationDto implements IDto {

    @JsonProperty("account")
    private String accountId;

    private String description;

    private double value;

    @JsonProperty("currency")
    private String currencyId;

    @JsonProperty("category")
    private String categoryId;

}
