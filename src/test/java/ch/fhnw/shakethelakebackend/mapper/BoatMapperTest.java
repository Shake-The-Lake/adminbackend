package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BoatMapperTest {
    @Autowired
    private BoatMapper mapper;

    private Boat boat;
    private BoatDto boatDto;
    private CreateBoatDto createBoatDto;

    @BeforeEach
    void setUp() {
        boat = new Boat();
        boat.setOperator("operator");

        Set<TimeSlot> timeSlots = new HashSet<>();
        timeSlots.add(TimeSlot.builder().id(1L).boat(boat).bookings(Set.of()).build());
        timeSlots.add(TimeSlot.builder().id(2L).boat(boat).bookings(Set.of()).build());
        boat.setTimeSlots(timeSlots);

        boatDto = new BoatDto();
        boatDto.setTimeSlotIds(Set.of(1L, 2L));
        boatDto.setOperator("operator");

        createBoatDto = new CreateBoatDto();
    }

    @Test
    void testToEntity() {
        Boat result = mapper.toEntity(createBoatDto);
        assertEquals(createBoatDto.getOperator(), result.getOperator());
    }

    @Test
    void testToDto() {
        BoatDto result = mapper.toDto(boat);
        assertEquals(boat.getOperator(), result.getOperator());
        assertEquals(2, result.getTimeSlotIds().size());
    }

    @Test
    void testToCreateDto() {
        CreateBoatDto result = mapper.toCreateDto(boat);
        assertEquals(boat.getOperator(), result.getOperator());
    }

    @Test
    void testPartialUpdate() {
        Boat newBoat = new Boat();
        newBoat.setOperator("newOperator");
        mapper.partialUpdate(boatDto, newBoat);
        assertEquals(boatDto.getOperator(), newBoat.getOperator());
    }

    @Test
    void testTimeSlotsToTimeSlotIds() {
        Set<Long> ids = mapper.timeSlotsToTimeSlotIds(boat.getTimeSlots());
        assertEquals(2, ids.size());
    }

    @Test
    void testTimeSlotsToTimeSlotIdsEmpty() {
        Set<Long> ids = mapper.timeSlotsToTimeSlotIds(new HashSet<>());
        assertEquals(0, ids.size());
    }

    @Test
    void testToDtoWithTimeSlotsAndActivityType() {
        BoatDto result = mapper.toDtoWithTimeSlotsAndActivityType(boat);
        assertEquals(boat.getOperator(), result.getOperator());
        assertEquals(2, result.getTimeSlotIds().size());
    }

    @Test
    void testToDtoWithTimeSlots() {
        BoatDto result = mapper.toDtoWithTimeSlots(boat);
        assertEquals(boat.getOperator(), result.getOperator());
        assertEquals(2, result.getTimeSlotIds().size());
    }

    @Test
    void testToDtoWithTimeSlotsNull() {
        BoatDto result = mapper.toDtoWithTimeSlots(null);
        assertEquals(null, result);
    }

    @Test
    void testToDtoWithTimeSlotsAndActivityTypeNull() {
        BoatDto result = mapper.toDtoWithTimeSlotsAndActivityType(null);
        assertEquals(null, result);
    }

    @Test
    void testToEntityNull() {
        Boat result = mapper.toEntity(null);
        assertEquals(null, result);
    }

    @Test
    void testToDtoNull() {
        BoatDto result = mapper.toDto(null);
        assertEquals(null, result);
    }

    @Test
    void testToCreateDtoNull() {
        CreateBoatDto result = mapper.toCreateDto(null);
        assertEquals(null, result);
    }
    
    @Test
    void testPartialUpdateNull() {
        Boat newBoat = new Boat();
        mapper.partialUpdate(null, newBoat);
        assertEquals(null, newBoat.getOperator());
    }

    @Test
    void testWithActivityType() {
        BoatDto result = mapper.toDtoWithActivityType(boat);
        assertEquals(boat.getOperator(), result.getOperator());
    }
}
