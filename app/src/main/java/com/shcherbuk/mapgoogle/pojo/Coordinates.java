package com.shcherbuk.mapgoogle.pojo;

public class Coordinates {

    private double start_lati;
    private double start_longi;

    private double finish_lati;
    private double finish_longi;

    private String color;

    public Coordinates() {

    }

    public Coordinates(double start_lati, double start_longi, double finish_lati, double finish_longi, String color) {
        this.start_lati = start_lati;
        this.start_longi = start_longi;
        this.finish_lati = finish_lati;
        this.finish_longi = finish_longi;
        this.color = color;
    }

    public double getStart_lati() {
        return start_lati;
    }

    public void setStart_lati(double start_lati) {
        this.start_lati = start_lati;
    }

    public double getStart_longi() {
        return start_longi;
    }

    public void setStart_longi(double start_longi) {
        this.start_longi = start_longi;
    }

    public double getFinish_lati() {
        return finish_lati;
    }

    public void setFinish_lati(double finish_lati) {
        this.finish_lati = finish_lati;
    }

    public double getFinish_longi() {
        return finish_longi;
    }

    public void setFinish_longi(double finish_longi) {
        this.finish_longi = finish_longi;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
