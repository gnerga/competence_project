package ui.hotspots;

import db.QueryExecutor;
import ui.common.OperationResponse;

class HotSpotService {
    final HotSpotValidator validator;
    final QueryExecutor queryExecutor;

    HotSpotService(HotSpotValidator validator, QueryExecutor queryExecutor) {
        this.validator = validator;
        this.queryExecutor = queryExecutor;
    }

    OperationResponse create(HotSpotCreateDto dto) {
        return validator.validate(dto).map(error -> OperationResponse.failure(error.reason)).orElse(createHotspot(dto));
    }

    private OperationResponse createHotspot(HotSpotCreateDto validDto) {
        queryExecutor.insert("siema");
        return null;
    }
}
