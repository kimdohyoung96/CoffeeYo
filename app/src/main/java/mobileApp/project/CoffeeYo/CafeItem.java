package mobileApp.project.CoffeeYo;

public class CafeItem {
    private String cafe_name;
    private double cafe_longitude;
    private double cafe_latitude;

    public CafeItem(){ }

    public CafeItem(String cafe_name, double cafe_longitude, double cafe_latitude) {
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
    }

    public String getCafe_name() { return cafe_name; }
    public double getCafe_longitude() { return cafe_longitude; }
    public double getCafe_latitude() { return cafe_latitude; }
}
