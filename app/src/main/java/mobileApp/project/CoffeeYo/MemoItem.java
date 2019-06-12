package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class MemoItem {
    private String cafe_name;
    private String menu;
    private String take;

    public MemoItem () { }

    public MemoItem(String cafe_name, String menu, String take) {
        this.cafe_name = cafe_name;
        this.menu = menu;
        this.take = take;
    }

    public String getCafe_name() {
        return cafe_name;
    }

    public String getMenu() {
        return menu;
    }

    public String getTake() {
        return take;
    }
}