package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class Orderfirebase {
    public String cafe_name;
    public String take;
    public String state;


    public Orderfirebase(){

    }


    public Orderfirebase(String cafe_name, String take, String state) {


        this.cafe_name = cafe_name;
        this.take = take;
        this.state = state;

    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cafe_name", cafe_name);
        result.put("take", take);
        result.put("state", state);
        return result;
    }
}