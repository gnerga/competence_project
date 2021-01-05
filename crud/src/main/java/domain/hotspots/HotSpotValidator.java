package domain.hotspots;

import java.util.Optional;

class HotSpotValidator {
    Optional<ValidationError> validate(HotSpotCreateDto createDto) {
        return Optional.empty();
    }
}
