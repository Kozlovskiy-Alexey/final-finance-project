package by.itacademy.account.scheduler.dto.util;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class IntegerLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public IntegerLocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        long epoch = Long.parseLong(p.getValueAsString());
        return Instant.ofEpochMilli(epoch).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
