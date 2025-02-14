package faang.school.postservice.filter;

import faang.school.postservice.dto.filter.FilterDto;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public interface Filter<T, F extends FilterDto> {
    boolean isApplicable(F filterDto);

    Stream<T> apply(Stream<T> objects, F filterDto);
}

