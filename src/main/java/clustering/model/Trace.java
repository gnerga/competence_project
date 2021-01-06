package clustering.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trace {
    private int userId;
    private String poisName;
    private Long enterTime;
    private Long exitTime;
    private String duration;
}
