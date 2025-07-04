package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class test {
    public static void main(String[] args) {
        Date date = new Date();
        Date expDate = new Date(date.getTime() + 3 * 3600 * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String dateToStr = dateFormat.format(date);
        String newDateToStr = dateFormat.format(expDate);
        System.out.println(dateToStr);
        System.out.println(newDateToStr);
    }
}
