import anonymization.Anonymizer;
import config.DbConfigResolver;
import config.PropertiesLoader;
import db.DbConfiguration;
import db.DbConnectionFactory;
import domain.fakseUsers.FakeUsersService;
import ui.CommandLineInterface;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        initializeDbConnectionFactory();

        CommandLineInterface cli = new CommandLineInterface(new OperationResponseResolver(), new CLIReader());
        CompletableFuture.runAsync(cli).join();

        Anonymizer anonymizer = new Anonymizer(new FakeUsersService());
        anonymizer.anonymize();
    }

    private static void initializeDbConnectionFactory() {
        PropertiesLoader loader = new PropertiesLoader();
        DbConfiguration dbConfiguration = new DbConfigResolver(loader).resolve();
        DbConnectionFactory.initialize(dbConfiguration);
    }
}
