package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchParameterMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private BookingMapper bookingMapper;

    @Mock
    private SearchParameterMapper searchParameterMapper;

    @Mock
    private BoatService boatService;

    @Mock
    private ActivityTypeService activityTypeService;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetSearchParameters() {
        // Given
        SearchParameterDto searchParameterDto = new SearchParameterDto();
        when(boatService.getAllBoats()).thenReturn(List.of());
        when(activityTypeService.getAllActivityTypes()).thenReturn(List.of());
        when(searchParameterMapper.toDto(any(), any())).thenReturn(searchParameterDto);

        // When
        SearchParameterDto result = searchService.getSearchParameters();

        // Then
        assertEquals(searchParameterDto, result);
    }

    @Test
    void testGetSearch() {
        // Given
        List<BookingDto> bookingDtoList = List.of(new BookingDto());
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(List.of(new Booking()));
        when(bookingMapper.toDtoExtended(any())).thenReturn(new BookingDto());

        // When
        List<BookingDto> result = searchService.getSearch(
                Optional.of("John"),
                Optional.of("Boat1"),
                Optional.of(LocalDateTime.now().minusDays(1)),
                Optional.of(LocalDateTime.now().plusDays(1)),
                Optional.of(1L)
        );

        // Then
        assertEquals(bookingDtoList.size(), result.size());
    }

    @Test
    void testGetSearchWithEmptyParameters() {
        // Given
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(List.of());

        // When
        List<BookingDto> result = searchService.getSearch(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        // Then
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetSearchWithOnlyPersonName() {
        // Given
        List<Booking> bookings = List.of(new Booking());
        List<BookingDto> bookingDtos = List.of(new BookingDto());
        when(bookingRepository.findAll(any(Specification.class))).thenReturn(bookings);
        when(bookingMapper.toDtoExtended(any())).thenReturn(new BookingDto());

        // When
        List<BookingDto> result = searchService.getSearch(
                Optional.of("John"),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        // Then
        assertEquals(bookingDtos.size(), result.size());
        verify(bookingRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void testGetSearchParametersWithNonEmptyLists() {
        // Given
        SearchParameterDto searchParameterDto = new SearchParameterDto();
        when(boatService.getAllBoats()).thenReturn(List.of(new BoatDto()));
        when(activityTypeService.getAllActivityTypes()).thenReturn(List.of(new ActivityTypeDto()));
        when(searchParameterMapper.toDto(any(), any())).thenReturn(searchParameterDto);

        // When
        SearchParameterDto result = searchService.getSearchParameters();

        // Then
        assertEquals(searchParameterDto, result);
        verify(boatService, times(1)).getAllBoats();
        verify(activityTypeService, times(1)).getAllActivityTypes();
        verify(searchParameterMapper, times(1)).toDto(any(), any());
    }

}
