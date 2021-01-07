package clustering;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ntp.TimeStamp;

import java.sql.Time;

@Getter
@Setter
public class Trace {
    private int userId;
    private String poisName;
    private Long enterTime;
    private Long exitTime;
    private String duration;
}
