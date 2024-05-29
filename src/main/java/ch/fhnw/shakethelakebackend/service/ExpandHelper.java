package ch.fhnw.shakethelakebackend.service;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class ExpandHelper {

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

    public <T> T applyExpansion(Optional<String> expand, String field, Function<Optional<Boolean>, T> expansionLogic) {
        return expansionLogic.apply(Optional.of(shouldExpand(expand, field)));
    }

    public <T> T applyExpansion(Optional<String> expand, Set<String> field,
        Function<Optional<Boolean>, T> expansionLogic) {
        return expansionLogic.apply(Optional.of(field.stream().allMatch(f -> shouldExpand(expand, f))));
    }

    public void applyExpansion(Optional<String> expand, String field, Consumer<Optional<Boolean>> expansionLogic) {
        expansionLogic.accept(Optional.of(shouldExpand(expand, field)));
    }
}
