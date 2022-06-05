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

import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"start_time", "stop_time", "interval", "time_unit"})
public class ScheduleDto implements IDto {

    @JsonProperty("start_time")
    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @JsonProperty("stop_time")
    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime stopTime;

    private int interval;

    @JsonProperty("time_unit")
    @Enumerated
    private String timeUnit;
}
