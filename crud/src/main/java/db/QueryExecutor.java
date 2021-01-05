package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {
    public <T> List<T> getList(String query, ResultSetTransformer<T> transformer) {
        var statement = DbConnectionFactory.getInstance().createStatement();

        try {
            statement.execute(query);
            var resultSet = statement.getResultSet();

            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(transformer.transform(resultSet));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not execute query: " + query);
    }

    public <T> Optional<T> get(String query, ResultSetTransformer<T> transformer) {
        var statement = DbConnectionFactory.getInstance().createStatement();

        try {
            statement.execute(query);
            var resultSet = statement.getResultSet();

            Optional<T> result = resultSet.next() ? Optional.of(transformer.transform(resultSet)) : Optional.empty();

            if (resultSet.next()) {
                throw new IllegalStateException("Query returned non-unique result!");
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not execute query: " + query);
    }

    public int insert(String insertQuery) {
        var statement = DbConnectionFactory.getInstance().createStatement();

        try {
            statement.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            var resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not insert query: " + insertQuery);
        }
    }

    public void execute(String query) {
        var statement = DbConnectionFactory.getInstance().createStatement();

        try {
            statement.execute(query);
            statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute query: " + query);
        }
    }

    public ResultSet getResultSet(String query) {
        var statement = DbConnectionFactory.getInstance().createStatement();

        try {
            statement.execute(query);
            return statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not execute query: " + query);
    }
}
