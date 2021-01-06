package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryExecutor {
    public <T> List<T> getList(String query, ResultSetTransformer<T> transformer) {
        ResultSet resultSet = null;

        try (Statement statement = DbConnectionFactory.getInstance().createStatement()){
            statement.execute(query);
            resultSet = statement.getResultSet();

            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                results.add(transformer.transform(resultSet));
            }

            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(resultSet != null && !resultSet.isClosed()) resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        throw new RuntimeException("Could not execute query: " + query);
    }

    public <T> Optional<T> get(String query, ResultSetTransformer<T> transformer) {
        ResultSet resultSet = null;
        try (Statement statement = DbConnectionFactory.getInstance().createStatement()) {
            statement.execute(query);
            resultSet = statement.getResultSet();

            Optional<T> result = resultSet.next() ? Optional.of(transformer.transform(resultSet)) : Optional.empty();

            if (resultSet.next()) {
                throw new IllegalStateException("Query returned non-unique result!");
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed())
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        throw new RuntimeException("Could not execute query: " + query);
    }

    public Optional<Integer> insert(String insertQuery) {
        ResultSet resultSet = null;
        try (Statement statement = DbConnectionFactory.getInstance().createStatement()) {
            statement.executeUpdate(insertQuery, Statement.RETURN_GENERATED_KEYS);
            resultSet = statement.getGeneratedKeys();
            return resultSet.next() ? Optional.of(resultSet.getInt(1)) : Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not insert query: " + insertQuery);
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed()) resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void execute(String query) {
        try (Statement statement = DbConnectionFactory.getInstance().createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not execute query: " + query);
        }
    }

    public ResultSet getResultSet(String query) {
        try (Statement statement = DbConnectionFactory.getInstance().createStatement()){
            statement.execute(query);
            return statement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not execute query: " + query);
    }
}
