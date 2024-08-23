package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.PersonMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchParameterMapper;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import ch.fhnw.shakethelakebackend.model.specification.SpecificationBooking;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SearchService {

    private final BookingRepository bookingRepository;
    private final SearchMapper searchMapper;
    private final SearchParameterMapper searchParameterMapper;
    private final ActivityTypeMapper activityTypeMapper;
    private final TimeSlotMapper timeSlotMapper;
    private final PersonMapper personMapper;
    private final BoatMapper boatMapper;
    private final BookingMapper bookingMapper;

    private final EventService eventService;

    public SearchParameterDto getSearchParameters(Long eventId) {
        Event event = eventService.getEvent(eventId);

        List<ActivityTypeDto> activityTypeDtos = event.getActivityTypes().stream().map(activityTypeMapper::toDto)
                .toList();
        List<BoatDto> boatDtos = event.getBoats().stream().map(boatMapper::toDto).toList();

        return searchParameterMapper.toDto(boatDtos, activityTypeDtos);
    }

    public List<SearchDto> getSearch(Long eventId, Optional<String> personName, Optional<Long> boatId,
            Optional<LocalTime> from, Optional<LocalTime> to, Optional<Long> activity) {
        List<SearchDto> searchDtos = List.of();
        List<Specification<Booking>> filterSpecification = new ArrayList<>();
        List<Specification<Booking>> searchSpecifications = new ArrayList<>();

        // Make search Specification for personName
        personName.ifPresent(name -> {
            for (String n :name.split(" ")) {
                searchSpecifications.add(new SpecificationBooking("person.firstName", ":", n));
                searchSpecifications.add(new SpecificationBooking("person.lastName", ":", n));
            }
        });


        // Make filter Specification for boatName, from, to, activity
        filterSpecification.add(new SpecificationBooking("timeSlot.boat.event.id", ":", eventId));
        boatId.ifPresent(name -> filterSpecification.add(new SpecificationBooking("timeSlot.boat.id", ":", name)));
        from.ifPresent(date -> filterSpecification.add(new SpecificationBooking("timeSlot.fromTime", ">=", date)));
        to.ifPresent(date -> filterSpecification.add(new SpecificationBooking("timeSlot.untilTime", "<=", date)));
        activity.ifPresent(
                act -> filterSpecification.add(new SpecificationBooking("timeSlot.activityType.id", ":", act)));

        searchDtos = bookingRepository.findAll(
                        Specification.allOf(filterSpecification)
                            .and(Specification.anyOf(searchSpecifications))).stream()
                .map(booking -> searchMapper.toDto(boatMapper.toDto(booking.getTimeSlot().getBoat()),
                        personMapper.toDto(booking.getPerson()), timeSlotMapper.toDto(booking.getTimeSlot()),
                        activityTypeMapper.toDto(booking.getTimeSlot().getActivityType()),
                        bookingMapper.toDto(booking))).toList();
        return searchDtos;
    }
}
