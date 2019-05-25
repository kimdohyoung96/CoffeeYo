package mobileApp.project.CoffeeYo;

import java.util.HashMap;
import java.util.Map;
public class FirebasePost {
    public String email;
    public String name;
    public String uid;
    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public FirebasePost(String email, String name, String uid) {
        this.email = email;
        this.name = name;
        this.uid = uid;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("uid", uid);
        return result;
    }
}