package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchParameterMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SearchMapper searchMapper;

    @Mock
    private ActivityTypeMapper activityTypeMapper;

    @Mock
    private BoatMapper boatMapper;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private SearchParameterMapper searchParameterMapper;

    @Mock
    private EventService eventService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetSearchParameters() {
        // Given
        Event event = Event.builder().id(1L).boats(Set.of()).activityTypes(Set.of()).build();
        SearchParameterDto searchParameterDto = new SearchParameterDto();
        when(eventService.getEvent(any())).thenReturn(event);
        when(searchParameterMapper.toDto(any(), any())).thenReturn(searchParameterDto);

        // When
        SearchParameterDto result = searchService.getSearchParameters(1L);

        // Then
        assertEquals(searchParameterDto, result);
    }

    @Test
    void testGetSearch() {
        // Given
        Event event = Event.builder().id(1L).build();
        Boat boat = Boat.builder().id(1L).name("Boat1").event(event).build();
        ActivityType activityType = ActivityType.builder().id(1L).name(new LocalizedString("en", "de", "ch")).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).fromTime(LocalTime.now()).untilTime(LocalTime.now())
                .activityType(activityType).boat(boat).build();
        Person person = Person.builder().id(1L).firstName("John").lastName("Doe").build();
        Booking booking = Booking.builder().id(1L).timeSlot(timeSlot).person(person).build();

        List<SearchDto> bookingDtoList = List.of(new SearchDto());
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(List.of(booking));
        when(searchMapper.toDto(any(), any(), any(), any(), any())).thenReturn(new SearchDto());

        // When
        List<SearchDto> result = searchService.getSearch(1L, Optional.of("John"), Optional.of("Boat1"),
                Optional.of(ZonedDateTime.now().minusDays(1)), Optional.of(ZonedDateTime.now().plusDays(1)),
                Optional.of(1L));

        // Then
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void testGetSearchWithEmptyParameters() {
        // Given
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // When
        List<SearchDto> result = searchService.getSearch(1L, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());

        // Then
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetSearchWithOnlyPersonName() {
        // Given
        Event event = Event.builder().id(1L).build();
        Boat boat = Boat.builder().id(1L).name("Boat1").event(event).build();
        ActivityType activityType = ActivityType.builder().id(1L).name(new LocalizedString("en", "de", "ch")).build();
        TimeSlot timeSlot = TimeSlot.builder().id(1L).fromTime(LocalTime.now()).untilTime(LocalTime.now())
                .activityType(activityType).boat(boat).build();
        Person person = Person.builder().id(1L).firstName("John").lastName("Doe").build();
        Booking booking = Booking.builder().id(1L).timeSlot(timeSlot).person(person).build();

        List<Booking> bookings = List.of(booking);
        List<BookingDto> bookingDtos = List.of(new BookingDto());
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(bookings);
        when(searchMapper.toDto(any(), any(), any(), any(), any())).thenReturn(new SearchDto());

        // When
        List<SearchDto> result = searchService.getSearch(1L, Optional.of("John"), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());

        // Then
        assertEquals(bookingDtos.size(), result.size());
        verify(bookingRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetSearchParametersWithNonEmptyLists() {
        // Given
        Boat boat = Boat.builder().id(1L).name("Boat1").build();
        BoatDto boatDto = BoatDto.builder().id(1L).name("Boat1").build();
        ActivityType activityType = ActivityType.builder().id(1L).name(new LocalizedString("en", "de", "ch")).build();
        ActivityTypeDto activityTypeDto = ActivityTypeDto.builder().id(1L).name(new LocalizedString("en", "de", "ch"))
                .build();
        Event event = Event.builder().id(1L).boats(Set.of(boat)).activityTypes(Set.of(activityType)).build();
        SearchParameterDto searchParameterDto = SearchParameterDto.builder().boats(List.of(boatDto))
                .activityTypes(List.of(activityTypeDto)).build();

        when(eventService.getEvent(any())).thenReturn(event);
        when(boatMapper.toDto(any())).thenReturn(boatDto);
        when(activityTypeMapper.toDto(any())).thenReturn(activityTypeDto);
        when(searchParameterMapper.toDto(any(), any())).thenReturn(searchParameterDto);

        // When
        SearchParameterDto result = searchService.getSearchParameters(1L);

        // Then
        assertEquals(searchParameterDto, result);
        verify(eventService, times(1)).getEvent(any());
        verify(searchParameterMapper, times(1)).toDto(any(), any());
    }

}
