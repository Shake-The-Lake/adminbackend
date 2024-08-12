package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.BookingDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.mapper.BookingMapper;
import ch.fhnw.shakethelakebackend.model.mapper.SearchParameterMapper;
import ch.fhnw.shakethelakebackend.model.repository.BookingRepository;
import ch.fhnw.shakethelakebackend.model.specification.SpecificationBooking;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SearchService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final SearchParameterMapper searchParameterMapper;

    private final BoatService boatService;
    private final ActivityTypeService activityTypeService;

    public SearchParameterDto getSearchParameters() {
        return searchParameterMapper.toDto(boatService.getAllBoats(), activityTypeService.getAllActivityTypes());
    }

    public List<BookingDto> getSearch(Optional<String> personName, Optional<String> boatName,
            Optional<ZonedDateTime> from, Optional<ZonedDateTime> to, Optional<Long> activity) {
        List<BookingDto> bookingDtos = List.of();
        List<Specification<Booking>> filterSpecification = new ArrayList<>();
        List<Specification<Booking>> searchSpecifications = new ArrayList<>();

        // Make search Specification for personName
        personName.ifPresent(name -> searchSpecifications.add(new SpecificationBooking("person.firstName", "?", name)));
        personName.ifPresent(name -> searchSpecifications.add(new SpecificationBooking("person.lastName", "?", name)));

        // Make filter Specification for boatName, from, to, activity
        personName.ifPresent(name -> searchSpecifications.add(new SpecificationBooking("person.firstName", "?", name)));
        boatName.ifPresent(name -> filterSpecification.add(new SpecificationBooking("timeSlot.boat.name", ":", name)));
        from.ifPresent(date -> filterSpecification.add(new SpecificationBooking("timeSlot.fromTime", ">=", date)));
        to.ifPresent(date -> filterSpecification.add(new SpecificationBooking("timeSlot.untilTime", "<=", date)));
        activity.ifPresent(
                act -> filterSpecification.add(new SpecificationBooking("timeSlot.activityType.id", ":", act)));
        bookingDtos = bookingRepository.findAll(
                        Specification.allOf(filterSpecification)
                                .and(Specification.anyOf(searchSpecifications))).stream()
                .map(bookingMapper::toDtoExtended).toList();
        return bookingDtos;
    }
}
