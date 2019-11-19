package feup.mieic.cmov.acme.ui.history;

public class ItemModel {

    private String transactionID;
    private String total;
    private String date;

    public ItemModel(String ID, String total, String date){
        this.transactionID = ID;
        this.total = total;
        this.date = date;
    }

    public String getTotal() {
        return total + "€";
    }

    public String getDate(){
        return date;
    }

    public String getID(){
        return transactionID;
    }
}
