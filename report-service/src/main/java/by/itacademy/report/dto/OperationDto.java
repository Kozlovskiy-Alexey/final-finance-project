package by.itacademy.report.dto;

import by.itacademy.report.dto.api.IDto;
import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeDeserializer;
import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"uuid", "dt_create", "dt_update", "date", "description", "category", "value", "currency"})
public class OperationDto implements IDto {

    private String uuid;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_create")
    private LocalDateTime dtCreate;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_update")
    private LocalDateTime dtUpdate;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("date")
    private LocalDateTime date;

    private String description;

    @JsonProperty("category")
    private String operationCategory;

    @JsonProperty("value")
    private double operationValue;

    @JsonProperty("currency")
    private String currencyUuid;

}


