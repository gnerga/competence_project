package anonymization;

import com.google.common.hash.Hashing;
import db.QueryExecutor;
import domain.fakseUsers.FakeUser;
import domain.fakseUsers.FakeUsersService;
import domain.users.UserResultSetMapper;
import model.User;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Anonymizer {
    private final QueryExecutor executor;
    private final LookupTable lookupTable;
    private final FakeUsersService fakeUsersService;

    public Anonymizer(FakeUsersService fakeUsersService) {
        this.fakeUsersService = fakeUsersService;
        this.executor = new QueryExecutor();
        this.lookupTable = new LookupTable(executor);
    }

    public void anonymize() {
        List<User> users = getUsers();
        List<User> fakeUsers = getFakeUsers();

        List<User> nonAnonymousUsers = getNonAnonymousUsers(users, fakeUsers);
        List<FakeUser> anonymousUsers = nonAnonymousUsers.stream().map(this::anonymize).collect(Collectors.toList());
        fakeUsersService.create(anonymousUsers);
    }

    private FakeUser anonymize(User user) {
        String newPhoneNumber = lookupTable.computeIfAbsent(user.getPhoneNumber(), this::anonymize);
        return new FakeUser(user, newPhoneNumber);
    }

    private String anonymize(String phoneNumber) {
        return Hashing.sha256().hashString(phoneNumber, StandardCharsets.UTF_8).toString().substring(0,12);
    }

    private List<User> getNonAnonymousUsers(List<User> users, List<User> fakeUsers) {
        Map<Integer, User> usersByIds = usersByIds(users);
        Map<Integer, User> fakeUsersByIds = usersByIds(fakeUsers);

        Set<Integer> nonAnonymousUsersIds = usersByIds.keySet().stream().filter(userId -> !fakeUsersByIds.containsKey(userId)).collect(Collectors.toSet());
        return nonAnonymousUsersIds.stream().map(usersByIds::get).collect(Collectors.toList());
    }

    private Map<Integer, User> usersByIds(Collection<User> users) {
        return users.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }

    private List<User> getUsers() {
        String query = "SELECT * FROM `users`";

        return executor.getList(query, new UserResultSetMapper());
    }

    private List<User> getFakeUsers() {
        String query = "SELECT * FROM `fake_users`";

        return executor.getList(query, new UserResultSetMapper());
    }
}
