package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {
    public <T> List<T> getList(String query, ResultSetTransformer<T> transformer) {
        return safeExecute(query, rs -> getList(rs, transformer));
    }

    private <T> List<T> getList(ResultSet resultSet, ResultSetTransformer<T> transformer) throws SQLException {
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(transformer.transform(resultSet));
        }

        return result;
    }

    public <T> Optional<T> get(String query, ResultSetTransformer<T> transformer) {
        return safeExecute(query, rs -> get(rs, transformer));
    }

    public <T> Optional<T> get(ResultSet resultSet, ResultSetTransformer<T> transformer) throws SQLException {
        Optional<T> result = resultSet.next() ? Optional.of(transformer.transform(resultSet)) : Optional.empty();

        if (resultSet.next()) {
            throw new IllegalStateException("Query returned non-unique result!");
        }

        return result;
    }

    public Optional<Integer> insert(String insertQuery) {
        return safeExecute(insertQuery, this::getId, (statement) -> insert(statement, insertQuery));
    }

    private Optional<Integer> getId(ResultSet resultSet) throws SQLException {
        return resultSet.next() ? Optional.of(resultSet.getInt(1)) : Optional.empty();
    }

    private ResultSet insert(Statement statement, String insertQuery) throws SQLException {
        statement.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
        return statement.getResultSet();
    }

    public void execute(String query) {
        safeExecute(query, (rs) -> null);
    }

    private <T> T safeExecute(String query, ResultSetMapper<T> rsMapper) {
        return safeExecute(query, rsMapper, (statement) -> {
            statement.execute(query);
            return statement.getResultSet();
        });
    }

    private <T> T safeExecute(String query, ResultSetMapper<T> rsMapper, StatementTransformer rsGetter) {
        try (Statement statement = DbConnectionFactory.getInstance().createStatement()) {
            try (ResultSet resultSet = rsGetter.mapper(statement)) {
                return rsMapper.map(resultSet);
            }
        } catch (SQLException ignored) {
        }

        throw new RuntimeException("Could not execute query: " + query);
    }

    @FunctionalInterface
    private interface StatementTransformer {
        ResultSet mapper(Statement statement) throws SQLException;
    }

    @FunctionalInterface
    private interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
