package ch.fhnw.shakethelakebackend.model.specification;

import ch.fhnw.shakethelakebackend.model.entity.Booking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;

/**
 *
 * Specification for booking
 *
 */
@AllArgsConstructor
public class SpecificationBooking implements Specification<Booking> {

    /**
     *
     * Search criteria
     *
     */
    public static class SearchCriteria {
        private final String key;
        private final String operation;
        private final Object value;

        public SearchCriteria(String key, String operation, Object value) {
            this.key = key;
            this.operation = operation;
            this.value = value;
        }
    }

    private SearchCriteria criteria;

    /**
     *
     * Create a new specification booking
     *
     * @param key of the search criteria
     * @param operation of the search criteria
     * @param value of the search criteria
     */
    public SpecificationBooking(String key, String operation, Object value) {
        criteria = new SearchCriteria(key, operation, value);
    }

    /**
     *
     * Create a new specification booking
     *
     * @param root of the booking
     * @param query of the booking
     * @param builder of the booking
     * @return Predicate for the booking
     */
    @Override
    public Predicate toPredicate(Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<?> path = root;
        for (String part : criteria.key.split("\\.")) {
            path = path.get(part);
        }

        switch (criteria.operation) {
        case "?":
            String value = "%" + criteria.value + "%";
            return builder.like(builder.lower(path.as(String.class)), value.toLowerCase());
        case ":":
            return builder.equal(path, criteria.value);
        case ">":
            return builder.greaterThan(path.as(LocalTime.class), (LocalTime) criteria.value);
        case ">=":
            return builder.greaterThanOrEqualTo(path.as(LocalTime.class), (LocalTime) criteria.value);
        case "<":
            return builder.lessThan(path.as(LocalTime.class), (LocalTime) criteria.value);
        case "<=":
            return builder.lessThanOrEqualTo(path.as(LocalTime.class), (LocalTime) criteria.value);
        default:
            return null;
        }
    }
}
