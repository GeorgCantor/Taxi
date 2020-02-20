package taxi.kassa.model.responses;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Withdraw {

    private String source_id;
    private String amount;
    private int date;
    private int status;

    public Withdraw(String source_id, String amount, int date, int status) {
        this.source_id = source_id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public int getStatusId() {
        return this.status;
    }

    public String getSource_id() {
        return this.source_id;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getDate() {
        long dv = (long) this.date * 1000;
        Date df = new java.util.Date(dv);
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");
        return dd.format(df);
    }

    public String getStatus() {
        int id = this.status;
        String result = "";

        if (id == 0) result = "Новая";
        else if (id == 1) result = "Одобрено";
        else if (id == 2) result = "Списано";
        else if (id == -1) result = "Оменено";

        return result;
    }

}
