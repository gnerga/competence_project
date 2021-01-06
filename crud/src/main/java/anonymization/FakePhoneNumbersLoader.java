package anonymization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FakePhoneNumbersLoader {
    public List<CreateFakePhoneNumberDto> load(Path fakePhoneNumbersPath) {
        try {
            return Files.lines(fakePhoneNumbersPath).map(CreateFakePhoneNumberDto::new).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Could not load fake phone numbers from file: " + fakePhoneNumbersPath.toString(), e);
        }
    }
}
