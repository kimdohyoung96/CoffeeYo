package mobileApp.project.CoffeeYo;

public class CafeItem {
    private int cafe_id;
    private String cafe_name;
    private double cafe_longitude;
    private double cafe_latitude;
    private int menu_cnt;
    private String menu1;
    private String menu2;
    private String menu3;

    public CafeItem(){ }

    public CafeItem(int cafe_id, String cafe_name, double cafe_longitude, double cafe_latitude, int menu_cnt, String menu1, String menu2, String menu3){
        this.cafe_id = cafe_id;
        this.cafe_name = cafe_name;
        this.cafe_longitude = cafe_longitude;
        this.cafe_latitude = cafe_latitude;
        this.menu_cnt = menu_cnt;
        this.menu1 = menu1;
        this.menu2 = menu2;
        this.menu3 = menu3;
    }

    public int getCafe_id() { return cafe_id; }
    public String getCafe_name() { return cafe_name; }
    public double getCafe_longitude() { return cafe_longitude; }
    public double getCafe_latitude() { return cafe_latitude; }
    public int getMenu_cnt() { return menu_cnt; }
    public String getMenu1() { return menu1; }
    public String getMenu2() { return  menu2; }
    public String getMenu3() { return  menu3; }
}
