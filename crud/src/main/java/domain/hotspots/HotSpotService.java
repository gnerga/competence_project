package domain.hotspots;

import db.QueryExecutor;
import model.HotSpot;
import ui.common.OperationResponse;

import java.util.List;
import java.util.Optional;

public class HotSpotService {
    final HotSpotValidator validator;
    final QueryExecutor queryExecutor;

    public HotSpotService(QueryExecutor queryExecutor) {
        this.validator = new HotSpotValidator();
        this.queryExecutor = queryExecutor;
    }

    public OperationResponse create(HotSpotCreateDto dto) {
        return validator.validate(dto).map(error -> OperationResponse.failure(error.reason)).orElse(createHotspot(dto));
    }

    private OperationResponse createHotspot(HotSpotCreateDto validDto) {
        try {
            String insert = "INSERT INTO hot_spots (name, description, longitude, latitude, type)"
                    + " VALUES (" + toSqlValues(validDto) + ");";
            queryExecutor.execute(insert);
            return OperationResponse.success("Hotspot created successfully!");
        } catch (RuntimeException e) {
            return OperationResponse.failure(e.getMessage());
        }
    }

    public List<HotSpot> findAll() {
        String query = "SELECT * FROM hot_spots";
        return queryExecutor.getList(query, new HotSpotResultSetMapper());
    }

    public Optional<HotSpot> findByName(String name) {
        String query = "SELECT * FROM hot_spots WHERE name = " + string(name);
        return queryExecutor.get(query, new HotSpotResultSetMapper());
    }

    private String toSqlValues(HotSpotCreateDto dto) {
        return string(dto.name) + ", " +
                string(dto.description) + ", " +
                dto.longitude + ", " +
                dto.latitude + ", " +
                string(dto.type.dbValue);
    }

    private String string(Object object) {
        return "'" + object.toString() + "'";
    }
}
