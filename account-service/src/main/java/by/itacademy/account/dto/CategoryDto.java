package by.itacademy.account.dto;

import by.itacademy.account.dto.api.IDto;
import by.itacademy.account.dto.util.serializator.IntegerLocalDateTimeDeserializer;
import by.itacademy.account.dto.util.serializator.IntegerLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonPropertyOrder({"uuid", "dt_create", "dt_update", "title"})
public class CategoryDto implements IDto {

    @JsonProperty("uuid")
    private String id;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_create")
    private LocalDateTime dtCreate;

    @JsonSerialize(using = IntegerLocalDateTimeSerializer.class)
    @JsonDeserialize(using = IntegerLocalDateTimeDeserializer.class)
    @JsonProperty("dt_update")
    private LocalDateTime dtUpdate;

    private String title;
}
