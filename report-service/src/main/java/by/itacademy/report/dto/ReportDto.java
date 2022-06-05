package by.itacademy.report.dto;

import by.itacademy.report.dto.api.ReportStatus;
import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeDeserializer;
import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeSerializer;
import by.itacademy.report.service.ReportType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"uuid", "dt_create", "dt_update", "status", "type", "description", "params"})
public class ReportDto implements Serializable {

    private String uuid;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_create")
    private LocalDateTime dtCreate;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_update")
    private LocalDateTime dtUpdate;

    @Enumerated
    private ReportStatus status;

    @Enumerated
    private ReportType type;

    private String description;

    private RequestParamsDto params;

}
