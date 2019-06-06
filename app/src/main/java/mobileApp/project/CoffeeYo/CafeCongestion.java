package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;

public class CafeCongestion {
    public String congestion;

    public CafeCongestion(){

    }

    public CafeCongestion(String congestion){
        this.congestion = congestion;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("congestion", congestion);
        return result;
    }
}
