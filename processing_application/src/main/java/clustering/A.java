package clustering;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class A implements Serializable {
    private String poisName;
    private Long enterTime;
    private Map<Long, String> test = new HashMap<>();

    public void addTime(A a){
        test.put(a.getEnterTime(),a.getPoisName());
    }

    public A() {
    }

    public A(String poisName, Long enterTime) {
        this.poisName = poisName;
        this.enterTime = enterTime;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        test.forEach((k,v) -> stringBuilder.append(k.toString()).append("---").append(v));
        return stringBuilder.toString();
    }
}
