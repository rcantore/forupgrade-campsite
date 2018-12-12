package com.upgrade.volcano.campsite.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.upgrade.volcano.campsite.dtos.BookingDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomBookingDTOSerializer extends StdSerializer<List<BookingDTO>> {

    protected CustomBookingDTOSerializer() {
        this(null);
    }

    protected CustomBookingDTOSerializer(Class<List<BookingDTO>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<BookingDTO> items,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        List<Long> ids = new ArrayList<>();
        for (BookingDTO item : items) {
            ids.add(item.getId());
        }
        generator.writeObject(ids);
    }

}
