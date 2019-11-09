package feup.mieic.cmov.acme.ui.order;

public class ProductModel {

    private String ID;
    private String name;
    private String qty;
    private String price;

    public ProductModel(String ID, String name, String qty, String price){
        this.ID = ID;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getID(){ return ID; }

    public String getName(){ return name; }

    public String getQty(){ return qty; }

    public String getPrice(){ return price; }

}
