package com.example.administrator.home_ex3;

/**
 * Created by AviramAlkobi on 10/01/2016.
 */
public class Item {

    private String temperature;
    private String date;
    private String time;
    private String info;
    private String url;

    public String getTemperature() {
            return this.temperature;
        }
    public void setTemperature(String temperature) {
        this.temperature = temperature+"c";
    }


    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }


    public String getInfo() {
        return this.info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public String get_iconUrl() {
        return this.url;
    }
    public void setUrl(String Url) {
        this.url = Url;
    }

}
