package anonymization;

import db.QueryExecutor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class LookupTable {
    private final QueryExecutor executor;
    private Map<String, String> table;

    LookupTable(QueryExecutor executor) {
        this.executor = executor;
    }

    String get(String key){
        return getTable().get(key);
    }

    String computeIfAbsent(String key, Function<String, String> computeFunction){
        return getTable().computeIfAbsent(key, computeFunction);
    }

    private Map<String, String> getTable() {
        if(table == null){
            table = loadLookupTable();
        }

        return table;
    }

    private Map<String, String> loadLookupTable() {
        String query = "SELECT * FROM `lookup_table`";

        return executor.getList(query, new Pn.PnResultSetMapper()).stream().collect(Collectors.toMap(Pn::getPhoneNumber, Pn::getFakePhoneNumber));
    }
}
