package ch.fhnw.shakethelakebackend.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class Expander {

    public Set<String> parseExpandParam(String expandParam) {
        if (expandParam != null && !expandParam.isEmpty()) {
            List<String> expandParams = Arrays.asList(expandParam.split(","));
            return new HashSet<>(expandParams.stream().map(String::trim).toList());
        } else {
            return new HashSet<>();
        }
    }

    public boolean shouldExpand(Optional<String> expand, String field) {
        if (expand.isEmpty()) {
            return false;
        }
        Set<String> expands = parseExpandParam(expand.get());
        return expands.contains(field);
    }

    public void applyExpansion(Optional<String> expand, String field, Runnable expansionLogic) {
        if (shouldExpand(expand, field)) {
            expansionLogic.run();
        }
    }
}
