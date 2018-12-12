package com.upgrade.volcano.campsite.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.upgrade.volcano.campsite.dtos.BookingDTO;
import com.upgrade.volcano.campsite.dtos.CampsiteDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomCampsiteDTOSerializer extends StdSerializer<CampsiteDTO> {

    protected CustomCampsiteDTOSerializer() {
        this(null);
    }

    protected CustomCampsiteDTOSerializer(Class<CampsiteDTO>  t) {
        super(t);
    }

    @Override
    public void serialize(
            CampsiteDTO item,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException {

        generator.writeObject(item.getId());
    }

}
