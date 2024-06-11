package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateBookingDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Person;
import ch.fhnw.shakethelakebackend.model.entity.TimeSlot;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotExtendedMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookingService {

    public static final String BOOKING_NOT_FOUND = "Booking not found";

    private final BookingRepository bookingRepository;
    private final TimeSlotService timeSlotService;
    private final PersonService personService;
    private final BookingMapper bookingMapper;
    private final Expander expander;
    private final PersonMapper personMapper;
    private final TimeSlotMapper timeSlotMapper;
    private final TimeSlotExtendedMapper timeSlotExtendedMapper;

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

    public BookingDto createBooking(CreateBookingDto bookingDto) {
        Booking booking = bookingMapper.toEntity(bookingDto);

        if (booking.getIsManual() && booking.getPagerNumber() == null) {
            throw new IllegalArgumentException("Pager number is required for manual bookings");
        }

        TimeSlot timeSlot = timeSlotService.getTimeSlot(bookingDto.getTimeSlotId());
        Person person = personService.getPerson(bookingDto.getPersonId());

        timeSlot.getBookings().add(booking);
        checkSeatsBooking(booking, timeSlot);

        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);

        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Booking not found"));
    }

    public BookingDto getBookingDto(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream().map(bookingMapper::toDto).collect(Collectors.toList());
    }

    public BookingDto updateBooking(Long id, CreateBookingDto bookingDto) {
        Booking booking = bookingMapper.toEntity(bookingDto);

        Booking oldBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        TimeSlot timeSlot = timeSlotService.getTimeSlot(bookingDto.getTimeSlotId());
        Person person = personService.getPerson(bookingDto.getPersonId());

        if (oldBooking.getTimeSlot().equals(timeSlot)) {
            timeSlot.getBookings().remove(oldBooking);
        }

        timeSlot.getBookings().add(booking);
        checkSeatsBooking(booking, timeSlot);

        booking.setTimeSlot(timeSlot);
        booking.setPerson(person);
        booking.setId(id);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }

    public BookingDto getBookingWithDetails(Long id, Optional<String> expand) {
        Booking booking = getBooking(id);
        BookingDto bookingDto = bookingMapper.toDto(booking);

        expander.applyExpansion(expand, "person", () -> {
            PersonDto personDto = personMapper.toDto(booking.getPerson());
            bookingDto.setPerson(personDto);
        });

        expander.applyExpansion(expand, "timeSlot", () -> {
            TimeSlotDto timeSlotDto = timeSlotMapper.toDto(booking.getTimeSlot());
            bookingDto.setTimeSlot(timeSlotDto);
        });

        expander.applyExpansion(expand, "timeSlot.boat", () -> {
            TimeSlotDto timeSlotDto = timeSlotExtendedMapper.toDtoWithBoat(booking.getTimeSlot());
            bookingDto.setTimeSlot(timeSlotDto);
        });

        return bookingDto;
    }
}
