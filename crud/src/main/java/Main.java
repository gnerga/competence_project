import anonymization.Anonymizer;
import config.DbConfigResolver;
import config.InputFilesConfigResolver;
import config.PropertiesLoader;
import db.DbConfiguration;
import db.DbConnectionFactory;
import domain.fakseUsers.FakeUsersService;
import ui.CommandLineInterface;
import ui.common.OperationResponseResolver;
import ui.io.CLIReader;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        PropertiesLoader loader = new PropertiesLoader();
        initializeDbConnectionFactory(loader);

        Anonymizer anonymizer = new Anonymizer(new FakeUsersService());
        anonymizer.initializeLookupTable(getFakePhoneNumbersFilePath(loader));

        CommandLineInterface cli = new CommandLineInterface(new OperationResponseResolver(), new CLIReader());
        CompletableFuture.runAsync(cli).join();

        anonymizer.anonymize();
    }

    private static void initializeDbConnectionFactory(PropertiesLoader loader) {
        DbConfiguration dbConfiguration = new DbConfigResolver(loader).resolve();
        DbConnectionFactory.initialize(dbConfiguration);
    }

    private static Path getFakePhoneNumbersFilePath(PropertiesLoader loader) {
        var inputResolver = new InputFilesConfigResolver(loader);
        return inputResolver.getFakePhoneNumbersLocation();
    }
}
