package domain.hotspots;

import db.ResultSetTransformer;
import model.HotSpot;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotSpotResultSetMapper implements ResultSetTransformer<HotSpot> {
    @Override
    public HotSpot transform(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String description = rs.getString("description");
        float longitude = rs.getFloat("longitude");
        float latitude = rs.getFloat("latitude");
        HotSpot.Type type = HotSpot.Type.of(rs.getString("type"));

        return new HotSpot(name, description, longitude, latitude, type);
    }
}
