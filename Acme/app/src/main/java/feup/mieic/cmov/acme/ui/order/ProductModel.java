package feup.mieic.cmov.acme.ui.order;

import java.util.UUID;

public class ProductModel {

    private UUID ID;
    private String name;
    private String qty;
    private String price;

    public ProductModel(UUID ID, String name, String qty, String price){
        this.ID = ID;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public UUID getID(){ return ID; }

    public String getName(){ return name; }

    public String getQty(){ return qty; }

    public String getPrice(){ return price; }

}
