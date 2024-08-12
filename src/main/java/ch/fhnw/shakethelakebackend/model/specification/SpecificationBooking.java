package ch.fhnw.shakethelakebackend.model.specification;

import ch.fhnw.shakethelakebackend.model.entity.Booking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

@AllArgsConstructor
public class SpecificationBooking implements Specification<Booking> {

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

    public SpecificationBooking(String key, String operation, Object value) {
        criteria = new SearchCriteria(key, operation, value);
    }

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
            return builder.greaterThan(path.as(ZonedDateTime.class), (ZonedDateTime) criteria.value);
        case ">=":
            return builder.greaterThanOrEqualTo(path.as(ZonedDateTime.class), (ZonedDateTime) criteria.value);
        case "<":
            return builder.lessThan(path.as(ZonedDateTime.class), (ZonedDateTime) criteria.value);
        case "<=":
            return builder.lessThanOrEqualTo(path.as(ZonedDateTime.class), (ZonedDateTime) criteria.value);
        default:
            return null;
        }
    }
}
