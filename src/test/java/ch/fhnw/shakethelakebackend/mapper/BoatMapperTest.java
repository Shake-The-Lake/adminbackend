package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BoatMapperTest {

    @InjectMocks
    private BoatMapper mapper = Mappers.getMapper(BoatMapper.class);

    private Boat boat;
    private BoatDto boatDto;
    private CreateBoatDto createBoatDto;

    @BeforeEach
    void setUp() {
        boat = new Boat();
        boat.setBoatDriver(new Person());

        Set<TimeSlot> timeSlots = new HashSet<>();
        timeSlots.add(new TimeSlot().builder().id(1L).build());
        timeSlots.add(new TimeSlot().builder().id(2L).build());
        boat.setTimeSlots(timeSlots);

        boatDto = new BoatDto();
        boatDto.setBoatDriverId(1L);
        boatDto.setTimeSlotIds(Set.of(1L, 2L));

        createBoatDto = new CreateBoatDto();
        createBoatDto.setBoatDriverId(1L);
    }

    @Test
    void testToEntity() {
        Boat result = mapper.toEntity(createBoatDto);
        assertEquals(createBoatDto.getBoatDriverId(), result.getBoatDriver().getId());
    }

    @Test
    void testToDto() {
        BoatDto result = mapper.toDto(boat);
        assertEquals(boat.getBoatDriver().getId(), result.getBoatDriverId());
        assertEquals(2, result.getTimeSlotIds().size());
    }

    @Test
    void testToCreateDto() {
        CreateBoatDto result = mapper.toCreateDto(boat);
        assertEquals(boat.getBoatDriver().getId(), result.getBoatDriverId());
    }

    @Test
    void testPartialUpdate() {
        Boat newBoat = new Boat();
        newBoat.setBoatDriver(new Person());
        mapper.partialUpdate(boatDto, newBoat);
        assertEquals(boatDto.getBoatDriverId(), newBoat.getBoatDriver().getId());
    }

    @Test
    void testTimeSlotsToTimeSlotIds() {
        Set<Long> ids = mapper.timeSlotsToTimeSlotIds(boat.getTimeSlots());
        assertEquals(2, ids.size());
    }
}
