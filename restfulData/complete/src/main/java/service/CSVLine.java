package service;

import java.text.ParseException;

public interface CSVLine
{
    public String toCSVString();
    public void importCSVString(String line) throws ParseException;
}
