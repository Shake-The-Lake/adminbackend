package ch.fhnw.shakethelakebackend.model.specification;

import ch.fhnw.shakethelakebackend.model.entity.Booking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@AllArgsConstructor
public class SpecificationBooking implements Specification<Booking> {

    public static class SearchCriteria {
        private String key;
        private String operation;
        private Object value;

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
        if (criteria == null) {
            return builder.conjunction();
        }

        if (criteria.operation.equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(root.get(criteria.key), criteria.value.toString());
        } else if (criteria.operation.equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(root.get(criteria.key), criteria.value.toString());
        } else if (criteria.operation.equalsIgnoreCase(":")) {
            if (root.get(criteria.key).getJavaType() == String.class) {
                return builder.like(root.get(criteria.key), "%" + criteria.value + "%");
            } else {
                return builder.equal(root.get(criteria.key), criteria.value);
            }
        }
        return null;
    }
}
