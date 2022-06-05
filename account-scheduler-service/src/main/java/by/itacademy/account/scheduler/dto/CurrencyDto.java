package by.itacademy.account.scheduler.dto;

import by.itacademy.account.scheduler.dto.api.IDto;
import by.itacademy.account.scheduler.dto.util.IntegerLocalDateTimeDeserializer;
import by.itacademy.account.scheduler.dto.util.IntegerLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({"uuid", "dtCreate", "dtUpdate", "title", "description"})
public class CurrencyDto implements IDto {

    @JsonProperty("uuid")
    private String id;

    @JsonProperty("dt_create")
    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime dtCreate;

    @JsonProperty("dt_update")
    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime dtUpdate;

    private String title;

    private String description;
}
