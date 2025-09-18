package model;

public class Equipment {
    private final String code;
    private String name;
    private int stock;

    public Equipment(String code, String name, int stock) {
        this.code = code;
        this.name = name;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setStock(int stock) { this.stock = stock; }

    public void add(int qty) { this.stock += qty; }
    public void remove(int qty) { this.stock -= qty; }

    @Override public String toString() {
        return name + " (" + stock + ")";
    }
}
