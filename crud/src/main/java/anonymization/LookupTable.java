package anonymization;

import db.QueryExecutor;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

class LookupTable {
    private final QueryExecutor executor;
    private Map<Integer, String> table;

    LookupTable(QueryExecutor executor) {
        this.executor = executor;
    }

    String get(int key) {
        return getTable().get(key);
    }

    boolean isEmpty() {
        String query = "SELECT NOT EXISTS (SELECT 1 FROM lookup_table)";
        return executor.get(query, (ResultSet rs) -> rs.getBoolean(1)).orElse(true);
    }

    int size() {
        String query = "SELECT COUNT(*) FROM lookup_table";
        return executor.get(query, (ResultSet rs) -> rs.getInt(1)).orElseThrow();
    }

    private Map<Integer, String> getTable() {
        if (table == null) {
            table = loadLookupTable();
        }

        return table;
    }

    private Map<Integer, String> loadLookupTable() {
        String query = "SELECT * FROM `lookup_table`";

        return executor.getList(query, new FakePhoneNumber.FakePhoneNumberResultSetMapper()).stream().collect(Collectors.toMap(FakePhoneNumber::getId, FakePhoneNumber::getFakePhoneNumber));
    }

    public void initialize(Collection<CreateFakePhoneNumberDto> fakeNumbers) {

        if (fakeNumbers.isEmpty()) {
            return;
        }

        String values = fakeNumbers.stream().map(this::toSqlValues).collect(Collectors.joining(", "));
        String insert = "INSERT INTO lookup_table (fake_phone_number) VALUES " + values;
        executor.insert(insert);
    }

    private String toSqlValues(CreateFakePhoneNumberDto number) {
        return "('" + number.phoneNumber + "')";
    }
}
