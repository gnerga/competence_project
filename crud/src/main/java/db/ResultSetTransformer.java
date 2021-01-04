package db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetTransformer<T> {
    T transform(ResultSet rs) throws SQLException;
}
