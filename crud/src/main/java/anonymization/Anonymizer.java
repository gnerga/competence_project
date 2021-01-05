package anonymization;

import com.google.common.hash.Hashing;
import db.QueryExecutor;
import domain.users.UserResultSetMapper;
import model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Anonymizer {
    private final QueryExecutor executor;

    public Anonymizer() {
        this.executor = new QueryExecutor();
    }

    public void anonymize(){
        List<User> users = getUsers();
        List<User> fakeUsers = getFakeUsers();
        List<Pn> lookupTable = getLookupTable();

        users.forEach(user -> {
            String fakePhoneNumber = Hashing.sha256().hashString(user.getPhoneNumber(), StandardCharsets.UTF_8).toString();

            fakeUsers.add(new User(user.getId(), fakePhoneNumber, user.profile));

            if(lookupTable.stream().noneMatch(pn -> pn.getPhoneNumber().equals(user.getPhoneNumber())))
                lookupTable.add(new Pn(user.getPhoneNumber(), fakePhoneNumber));
        });
    }

    private List<User> getUsers(){
        String query = "SELECT * FROM `users`";

        return executor.getList(query, new UserResultSetMapper());
    }
    private List<User> getFakeUsers(){
        String query = "SELECT * FROM `fake_users`";

        return executor.getList(query, new UserResultSetMapper());
    }

    private List<Pn> getLookupTable(){
        String query = "SELECT * FROM `lookup_table`";

        return executor.getList(query, new Pn.PnResultSetMapper());
    }
}
