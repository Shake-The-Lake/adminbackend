package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private BookingService bookingService;

    private TimeSlot createTimeSlot(Long id) {
        TimeSlot timeSlot = new TimeSlot();
        Boat boat = new Boat();
        boat.setId(1L);
        boat.setSeatsRider(2);
        boat.setSeatsViewer(2);
        timeSlot.setId(id);
        timeSlot.setBoat(boat);
        timeSlot.setBookings(new HashSet<>());
        return timeSlot;
    }

    @Test
    void testGetBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.getBooking(bookingId);

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void testGetBookingNotFound() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBooking(bookingId));

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void testCreateBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setIsRider(true);
        booking.setIsManual(false);

        Person person = new Person();

        TimeSlot timeSlot = createTimeSlot(1L);
        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        when(timeSlotService.getTimeSlot(timeSlot.getId())).thenReturn(timeSlot);
        when(personService.getPerson(person.getId())).thenReturn(person);
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.createBooking(booking);

        verify(timeSlotService).getTimeSlot(timeSlot.getId());
        verify(personService).getPerson(person.getId());
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCreateBookingOnFullTimeSlot() {
        TimeSlot timeSlot = createTimeSlot(1L);
        Person person = new Person();

        Booking booking1 = new Booking(1L, true, true, 1, person, timeSlot);
        Booking booking2 = new Booking(2L, true, true, 1, person, timeSlot);

        timeSlot.getBookings().add(booking1);
        timeSlot.getBookings().add(booking2);

        Booking booking = new Booking();
        booking.setIsRider(true);
        booking.setIsManual(false);
        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        when(timeSlotService.getTimeSlot(timeSlot.getId())).thenReturn(timeSlot);
        when(personService.getPerson(person.getId())).thenReturn(person);

        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(booking));

        verify(timeSlotService).getTimeSlot(timeSlot.getId());
        verify(personService).getPerson(person.getId());
    }

    @Test
    void testUpdateBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setIsRider(true);

        Person person = new Person();
        person.setId(1L);

        TimeSlot timeSlot = createTimeSlot(1L);
        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        booking.setPerson(person);
        booking.setTimeSlot(timeSlot);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(timeSlotService.getTimeSlot(timeSlot.getId())).thenReturn(timeSlot);
        when(personService.getPerson(person.getId())).thenReturn(person);
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.updateBooking(booking.getId(), booking);

        verify(bookingRepository).findById(booking.getId());
        verify(timeSlotService).getTimeSlot(timeSlot.getId());
        verify(personService).getPerson(person.getId());
        verify(bookingRepository).save(booking);
    }

    @Test
    void testUpdateBookingToViewer() {
        TimeSlot timeSlot = createTimeSlot(1L);
        Person person = new Person();

        Booking booking1 = new Booking(1L, true, true, 1, person, timeSlot);
        Booking booking2 = new Booking(2L, true, true, 1, person, timeSlot);

        timeSlot.getBookings().add(booking1);
        timeSlot.getBookings().add(booking2);

        Booking booking = new Booking();
        booking.setIsRider(false);
        booking.setIsManual(true);
        booking.setPagerNumber(1);
        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        when(bookingRepository.findById(booking1.getId())).thenReturn(Optional.of(booking1));
        when(timeSlotService.getTimeSlot(timeSlot.getId())).thenReturn(timeSlot);
        when(personService.getPerson(person.getId())).thenReturn(person);

        bookingService.updateBooking(booking1.getId(), booking);

        verify(timeSlotService).getTimeSlot(timeSlot.getId());
        verify(personService).getPerson(person.getId());
    }

    @Test
    void testUpdateBookingNotFound() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.updateBooking(bookingId, booking));

        verify(bookingRepository).findById(bookingId);
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        bookingService.deleteBooking(bookingId);

        verify(bookingRepository).findById(bookingId);
        verify(bookingRepository).delete(booking);
    }

    @Test
    void testDeleteBookingNotFound() {
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.deleteBooking(bookingId));

        verify(bookingRepository).findById(bookingId);
    }

}
