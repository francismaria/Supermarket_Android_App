package feup.mieic.cmov.acme.ui.history;

public class ItemModel {

    private String total;
    private String date;

    public ItemModel(String total, String date){
        this.total = total;
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public String getDate(){
        return date;
    }
}
