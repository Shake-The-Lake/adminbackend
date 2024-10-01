package ch.fhnw.shakethelakebackend.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * Expander class
 *
 */
@Component
public class Expander {

    /**
     * Parse the expand parameter
     *
     * @param expandParam the expand parameter
     * @return a set of strings
     */
    public Set<String> parseExpandParam(String expandParam) {
        if (expandParam != null && !expandParam.isEmpty()) {
            List<String> expandParams = Arrays.asList(expandParam.split(","));
            return new HashSet<>(expandParams.stream().map(String::trim).toList());
        } else {
            return new HashSet<>();
        }
    }

    /**
     * Check if the field should be expanded
     *
     * @param expand the expand parameter
     * @param field the field to be expanded
     * @return a boolean
     */
    public boolean shouldExpand(Optional<String> expand, String field) {
        if (expand.isEmpty()) {
            return false;
        }
        Set<String> expands = parseExpandParam(expand.get());
        return expands.contains(field);
    }

    /**
     * Apply the expansion logic
     *
     * @param expand the expand parameter
     * @param field the field to be expanded
     * @param expansionLogic the logic to be applied
     */
    public void applyExpansion(Optional<String> expand, String field, Runnable expansionLogic) {
        if (shouldExpand(expand, field)) {
            expansionLogic.run();
        }
    }
}
