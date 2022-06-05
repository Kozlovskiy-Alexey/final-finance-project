package by.itacademy.account.scheduler.dto;

import by.itacademy.account.scheduler.dto.api.IDto;
import by.itacademy.account.scheduler.dto.util.IntegerLocalDateTimeDeserializer;
import by.itacademy.account.scheduler.dto.util.IntegerLocalDateTimeSerializer;
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
@JsonPropertyOrder({"uuid", "dt_create", "dt_update", "schedule", "operation"})
public class ScheduledOperationDto implements IDto {

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

    @JsonProperty("schedule")
    private ScheduleDto scheduleDto;

    @JsonProperty("operation")
    private OperationDto operationDto;
}
