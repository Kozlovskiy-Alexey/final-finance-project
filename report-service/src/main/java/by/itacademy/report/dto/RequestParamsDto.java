package by.itacademy.report.dto;

import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeDeserializer;
import by.itacademy.report.dto.util.serializer.IntegerLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamsDto {

    private List<String> accounts;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime from;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    private LocalDateTime to;

    private List<String> categories;
}
