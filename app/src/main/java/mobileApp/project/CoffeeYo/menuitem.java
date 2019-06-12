package mobileApp.project.CoffeeYo;

public class menuitem {
    private String menu;
    private String price;
    private String count;

    public menuitem(String menu, String price, String count) {

        this.menu = menu;
        this.price = price;
        this.count = count;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMenu() {
        return menu;
    }
    public String getPrice() {
        return price;
    }
    public String getCount() {
        return count;
    }

}