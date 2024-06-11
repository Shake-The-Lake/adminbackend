package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotExtendedMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TimeSlotMapperExtendedMapperTest {

    @Mock
    private BoatMapper boatMapper;
    @InjectMocks
    private TimeSlotExtendedMapper timeSlotMapper = Mappers.getMapper(TimeSlotExtendedMapper.class);

    @Test
    void testTimeSlotMapperWithBoatName() {
        Boat boat = Boat.builder().name("Boat").build();
        TimeSlot timeSlot = TimeSlot.builder().boat(boat).bookings(Set.of()).build();
        BoatDto boatDto = new BoatDto();
        boatDto.setName("Boat");
        when(boatMapper.toDto(boat)).thenReturn(boatDto);

        TimeSlotDto timeSlotDto = timeSlotMapper.toDtoWithBoat(timeSlot);

        assertEquals("Boat", timeSlotDto.getBoat().getName());
    }
}
