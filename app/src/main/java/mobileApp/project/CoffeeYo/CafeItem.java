package mobileApp.project.CoffeeYo;

public class CafeItem {
    private int manager_id;
    private String cafe_name;
    private double cafe_longitude;
    private double cafe_latitude;
    private String menu1;
    private String menu2;
    private String menu3;
    private String menu4;
    private String menu5;

    public CafeItem(){ }

    public CafeItem(int manager_id, String cafe_name, double cafe_longitude, double cafe_latitude, String menu1, String menu2, String menu3, String menu4, String menu5){
        this.manager_id = manager_id;
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu3 = menu3;
        this.menu4 = menu4;
        this.menu5 = menu5;
    }

    public int getManager_id() { return manager_id; }
    public String getCafe_name() { return cafe_name; }
    public double getCafe_longitude() { return cafe_longitude; }
    public double getCafe_latitude() { return cafe_latitude; }
    public String getMenu1() { return menu1; }
    public String getMenu2() { return menu2; }
    public String getMenu3() { return menu3; }
    public String getMenu4() { return menu4; }
    public String getMenu5() { return menu5; }
}
