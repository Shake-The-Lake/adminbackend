package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.entity.enums.PersonType;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TimeSlotService timeSlotService;

    @Mock
    private PersonService personService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @Mock
    private BoatMapper boatMapper;

    @Mock
    private Expander expander;

    @InjectMocks
    private BookingService bookingService;

    private Person person;
    private Boat boat;
    private TimeSlot timeSlot;
    private Booking booking;

    private BookingDto bookingDto;

    private CreateBookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        ZonedDateTime fromTimeZoned = ZonedDateTime.now();
        ZonedDateTime untilTimeZoned = ZonedDateTime.now().plusHours(1);
        boat = Boat.builder().seatsRider(2).seatsViewer(2).id(1L).name("boat").build();
        person = Person.builder().id(1L).firstName("John").lastName("Doe").emailAddress("john.doe@example.com")
                .phoneNumber("123456789").personType(PersonType.CUSTOMER).build();
        timeSlot = TimeSlot.builder().boat(boat).id(1L).fromTime(fromTimeZoned).untilTime(untilTimeZoned)
                .bookings(new HashSet<>()).build();
        booking = Booking.builder().isRider(true).isManual(false).timeSlot(timeSlot).id(1L).person(person).build();

        bookingDto = BookingDto.builder().id(1L).isRider(true).isManual(false).timeSlotId(1L).personId(1L).build();
        createBookingDto = CreateBookingDto.builder().isRider(true).isManual(false).timeSlotId(1L).personId(1L).build();

    }

    @Test
    void testCreateBooking() {
        when(bookingMapper.toEntity(any(CreateBookingDto.class))).thenReturn(booking);
        when(timeSlotService.getTimeSlot(booking.getTimeSlot().getId())).thenReturn(timeSlot);
        when(personService.getPerson(booking.getPerson().getId())).thenReturn(person);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(createBookingDto);

        assertEquals(bookingDto, result);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testUpdateBooking() {

        when(bookingMapper.toEntity(any(CreateBookingDto.class))).thenReturn(booking);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(personService.getPerson(booking.getPerson().getId())).thenReturn(person);
        when(timeSlotService.getTimeSlot(booking.getTimeSlot().getId())).thenReturn(timeSlot);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.updateBooking(1L, createBookingDto);

        assertEquals(bookingDto, result);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testGetBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBooking(1L);

        assertEquals(booking, result);
    }

    @Test
    void testGetBookingDto() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingDto(1L);

        assertEquals(bookingDto, result);
    }

    @Test
    void testDeleteBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(1L);

        verify(bookingRepository).delete(booking);
    }

    @Test
    void testOverBookedSeats() {
        booking.setIsRider(true);
        booking.setPerson(person);
        booking.setTimeSlot(timeSlot);
        Booking booking1 = Booking.builder().isRider(true).isManual(false).timeSlot(timeSlot).id(2L).person(person)
                .build();
        Booking booking2 = Booking.builder().isRider(true).isManual(false).timeSlot(timeSlot).id(3L).person(person)
                .build();
        timeSlot.getBookings().add(booking1);
        timeSlot.getBookings().add(booking2);

        when(bookingMapper.toEntity(any(CreateBookingDto.class))).thenReturn(booking);
        when(timeSlotService.getTimeSlot(booking.getTimeSlot().getId())).thenReturn(timeSlot);
        when(personService.getPerson(booking.getPerson().getId())).thenReturn(person);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void testBookingWithDetails() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingWithDetails(1L, Optional.of("person"));

        verify(expander, times(3)).applyExpansion(any(), any(), any());
    }

    @Test
    void testCreateBookingNoSeatsAvailableForRiders() {
        booking.setIsRider(true);
        booking.setPerson(person);
        booking.setTimeSlot(timeSlot);
        Booking booking1 = Booking.builder().isRider(true).isManual(false).timeSlot(timeSlot).id(2L).person(person)
                .build();
        Booking booking2 = Booking.builder().isRider(true).isManual(false).timeSlot(timeSlot).id(3L).person(person)
                .build();
        timeSlot.getBookings().add(booking1);
        timeSlot.getBookings().add(booking2);

        when(bookingMapper.toEntity(any(CreateBookingDto.class))).thenReturn(booking);
        when(timeSlotService.getTimeSlot(booking.getTimeSlot().getId())).thenReturn(timeSlot);
        when(personService.getPerson(booking.getPerson().getId())).thenReturn(person);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void testCreateBookingNoSeatsAvailableForViewers() {
        booking.setIsRider(false);
        booking.setPerson(person);
        booking.setTimeSlot(timeSlot);
        Boat boat = Boat.builder().seatsRider(1).seatsViewer(1).build();
        timeSlot.setBoat(boat);
        Booking booking1 = Booking.builder().isRider(false).isManual(false).timeSlot(timeSlot).id(2L).person(person)
                .build();
        timeSlot.getBookings().add(booking1);

        when(bookingMapper.toEntity(any(CreateBookingDto.class))).thenReturn(booking);
        when(timeSlotService.getTimeSlot(booking.getTimeSlot().getId())).thenReturn(timeSlot);
        when(personService.getPerson(booking.getPerson().getId())).thenReturn(person);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(createBookingDto));
    }

    @Test
    void testGetBookingInvalidId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(999L));
    }

    @Test
    void testDeleteBookingInvalidId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.deleteBooking(999L));
    }

}
