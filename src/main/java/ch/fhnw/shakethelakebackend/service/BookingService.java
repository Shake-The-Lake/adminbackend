package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TimeSlotService timeSlotService;
    private final PersonService personService;

    // Check if there is a seat available for the booking otherwise throw an exception
    private void checkSeatsBooking(Booking booking, TimeSlot timeSlot) {
        Set<Booking> bookings = timeSlot.getBookings();
        Boat boat = timeSlot.getBoat();

        long rides = bookings.stream().filter(Booking::getIsRider).count();
        long viewers = bookings.size() - rides;

        if (booking.getIsRider() && rides > boat.getSeatsRider()) {
            throw new IllegalArgumentException("No more seats available for riders");
        } else if (!booking.getIsRider() && viewers > boat.getSeatsViewer()) {
            throw new IllegalArgumentException("No more seats available for viewers");
        }
    }

    public Booking createBooking(Booking booking) {

        if (booking.getId() != null && bookingRepository.existsById(booking.getId())) {
            throw new IllegalArgumentException("Booking already exists");
        }

        if (booking.getIsManual() && booking.getPagerNumber() == null) {
            throw new IllegalArgumentException("Pager number is required for manual bookings");
        }

        TimeSlot timeSlot = timeSlotService.getTimeSlot(booking.getTimeSlot().getId());
        Person person = personService.getPerson(booking.getPerson().getId());

        timeSlot.getBookings().add(booking);
        checkSeatsBooking(booking, timeSlot);

        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        return bookingRepository.save(booking);
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    public Booking updateBooking(Long id, Booking booking) {

        Booking oldBooking = bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        TimeSlot timeSlot = timeSlotService.getTimeSlot(booking.getTimeSlot().getId());
        Person person = personService.getPerson(booking.getPerson().getId());

        if (oldBooking.getTimeSlot().equals(timeSlot)) {
            timeSlot.getBookings().remove(oldBooking);
        }

        timeSlot.getBookings().add(booking);
        checkSeatsBooking(booking, timeSlot);

        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);
        booking.setId(id);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }
}
