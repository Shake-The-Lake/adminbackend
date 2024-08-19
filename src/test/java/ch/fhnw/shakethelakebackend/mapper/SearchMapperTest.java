package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import ch.fhnw.shakethelakebackend.model.mapper.SearchMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class SearchMapperTest {

    @InjectMocks
    private SearchMapper searchMapper = Mappers.getMapper(SearchMapper.class);

    @Test
    void mapToSearchDto() {
        // Given

        ActivityTypeDto activityType = ActivityTypeDto.builder().id(1L).name(new LocalizedString("en", "de", "ch"))
                .build();
        TimeSlotDto timeSlot = TimeSlotDto.builder().id(1L).fromTime(LocalTime.now()).untilTime(LocalTime.now())
                .build();
        BoatDto boat = BoatDto.builder().id(1L).name("Boat").timeSlots(Set.of(timeSlot)).build();
        PersonDto person = PersonDto.builder().id(1L).firstName("John").lastName("Doe").build();
        BookingDto booking = BookingDto.builder().id(1L).build();
        // When

        SearchDto searchDto = searchMapper.toDto(boat, person, timeSlot, activityType, booking);
        // Then
        assertEquals(boat.getId(), searchDto.getBoat().getId());
        assertEquals(boat.getName(), searchDto.getBoat().getName());
        assertEquals(person.getId(), searchDto.getPerson().getId());
        assertEquals(person.getFirstName(), searchDto.getPerson().getFirstName());
        assertEquals(person.getLastName(), searchDto.getPerson().getLastName());
        assertEquals(timeSlot.getId(), searchDto.getTimeSlot().getId());
        assertEquals(timeSlot.getFromTime(), searchDto.getTimeSlot().getFromTime());
        assertEquals(timeSlot.getUntilTime(), searchDto.getTimeSlot().getUntilTime());
        assertEquals(activityType.getId(), searchDto.getActivityType().getId());
        assertEquals(activityType.getName().getEn(), searchDto.getActivityType().getName().getEn());
    }

    @Test
    void mapToSearchDtoNull() {
        // Given
        BoatDto boat = BoatDto.builder().build();
        ActivityTypeDto activityType = ActivityTypeDto.builder().build();
        TimeSlotDto timeSlot = TimeSlotDto.builder().build();
        PersonDto person = PersonDto.builder().build();
        BookingDto booking = BookingDto.builder().build();
        // When
        SearchDto searchDto = searchMapper.toDto(boat, person, timeSlot, activityType, booking);
        // Then
        assertEquals(boat.getId(), searchDto.getBoat().getId());
        assertEquals(boat.getName(), searchDto.getBoat().getName());
        assertEquals(person.getId(), searchDto.getPerson().getId());
        assertEquals(person.getFirstName(), searchDto.getPerson().getFirstName());
        assertEquals(person.getLastName(), searchDto.getPerson().getLastName());
        assertEquals(timeSlot.getId(), searchDto.getTimeSlot().getId());
        assertEquals(timeSlot.getFromTime(), searchDto.getTimeSlot().getFromTime());
        assertEquals(timeSlot.getUntilTime(), searchDto.getTimeSlot().getUntilTime());
        assertEquals(activityType.getId(), searchDto.getActivityType().getId());
    }

    @Test
    void mapToSearchDtoNullValues() {
        // Given
        BoatDto boat = BoatDto.builder().id(1L).build();
        ActivityTypeDto activityType = ActivityTypeDto.builder().id(1L).build();
        TimeSlotDto timeSlot = TimeSlotDto.builder().id(1L).build();
        PersonDto person = PersonDto.builder().id(1L).build();
        BookingDto booking = BookingDto.builder().id(1L).build();
        // When
        SearchDto searchDto = searchMapper.toDto(boat, person, timeSlot, activityType, booking);
        // Then
        assertEquals(boat.getId(), searchDto.getBoat().getId());
        assertEquals(boat.getName(), searchDto.getBoat().getName());
        assertEquals(person.getId(), searchDto.getPerson().getId());
        assertEquals(person.getFirstName(), searchDto.getPerson().getFirstName());
        assertEquals(person.getLastName(), searchDto.getPerson().getLastName());
        assertEquals(timeSlot.getId(), searchDto.getTimeSlot().getId());
        assertEquals(timeSlot.getFromTime(), searchDto.getTimeSlot().getFromTime());
        assertEquals(timeSlot.getUntilTime(), searchDto.getTimeSlot().getUntilTime());
        assertEquals(activityType.getId(), searchDto.getActivityType().getId());
    }

    @Test
    void mapToSearchDtoNullValuesNull() {
        // Given
        BoatDto boat = null;
        ActivityTypeDto activityType = null;
        TimeSlotDto timeSlot = null;
        PersonDto person = null;
        BookingDto booking = null;
        // When
        SearchDto searchDto = searchMapper.toDto(boat, person, timeSlot, activityType, booking);
        // Then
        assertNull(searchDto);
    }
}
