package web;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAvgPx {

    private int x;
    private double value;
    private long duration;

    public GetAvgPx() {
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		AvgPxResult avgPx = new RestTemplate().getForObject("http://localhost:8080/AvgPxService?x="+x, AvgPxResult.class);
		value = avgPx.getPx();
		duration = avgPx.getDuration();
	}

	public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AvgPx [x=" + x + ", value=" + value + "]";
	}

   
}
