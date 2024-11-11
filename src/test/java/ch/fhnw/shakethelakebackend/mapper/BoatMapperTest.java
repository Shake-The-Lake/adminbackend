package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.config.FirebaseConfigTest;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBoatDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Import(FirebaseConfigTest.class)
class BoatMapperTest {

    @Mock
    private TimeSlotMapper timeSlotMapper;
    @InjectMocks
    private BoatMapper boatMapper = Mappers.getMapper(BoatMapper.class);


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
        CreateBoatDto createBoatDto = new CreateBoatDto();
        createBoatDto.setEventId(1L);

        Boat boat = boatMapper.toEntity(createBoatDto);

        assertNotNull(boat);
        assertEquals(1L, boat.getEvent().getId());
    }

    @Test
    void testToDto() {
        Boat boat = new Boat();
        boat.setId(1L);
        boat.setEvent(Event.builder().id(1L).build());
        boat.setTimeSlots(Set.of(TimeSlot.builder().id(1L).build(), TimeSlot.builder().id(2L).build()));

        BoatDto boatDto = boatMapper.toDto(boat);

        assertNotNull(boatDto);
        assertEquals(1L, boatDto.getEventId());
        assertEquals(Set.of(1L, 2L), boatDto.getTimeSlotIds());
    }

    @Test
    void testToDtoWithTimeSlots() {
        Boat boat = new Boat();
        boat.setId(1L);
        boat.setEvent(Event.builder().id(1L).build());
        boat.setTimeSlots(Set.of(TimeSlot.builder().id(1L).build(), TimeSlot.builder().id(2L).build()));

        BoatDto boatDto = boatMapper.toDtoWithTimeSlots(boat);

        assertNotNull(boatDto);
        assertEquals(1L, boatDto.getEventId());
        assertEquals(Set.of(1L, 2L), boatDto.getTimeSlotIds());
    }

    @Test
    void testTimeSlotsToTimeSlotIds() {
        Set<TimeSlot> timeSlots = Set.of(TimeSlot.builder().id(1L).build(), TimeSlot.builder().id(2L).build());

        Set<Long> timeSlotIds = boatMapper.timeSlotsToTimeSlotIds(timeSlots);

        assertNotNull(timeSlotIds);
        assertEquals(Set.of(1L, 2L), timeSlotIds);
    }


}
