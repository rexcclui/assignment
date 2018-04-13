package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Price implements CSVLine
{

    static SimpleDateFormat df2 = new SimpleDateFormat("yyMMdd_HHmmss.SSS");
    @Override
    public String toString() {
        return "Price [updateTime=" + df2.format(new Date(updateTime)) + ", value=" + value + "]";
    }

    private long updateTime;

    Price() {
        super();
    }
    Price(double value, long updateTime) {
        super();
        this.value = value;
        this.updateTime = updateTime;
    }

    private double value;

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toCSVString() {
        return df2.format(new Date(updateTime)) +"," + value;
        
    }

    @Override
    public void importCSVString(String line) throws ParseException {
        updateTime = df2.parse( line.split(",")[0]).getTime();
        value = Double.parseDouble(line.split(",")[1]);
    }

}
