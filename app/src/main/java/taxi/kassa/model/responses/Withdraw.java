package taxi.kassa.model.responses;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import static taxi.kassa.util.Constants.APPROVED;
import static taxi.kassa.util.Constants.CANCELED;
import static taxi.kassa.util.Constants.NEW;
import static taxi.kassa.util.Constants.WRITTEN_OFF;

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

    public String getHours() {
        long dv = (long) this.date * 1000;
        Date df = new java.util.Date(dv);
        SimpleDateFormat dd = new SimpleDateFormat("HH:mm");

        return dd.format(df);
    }

    private static DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {

        @Override
        public String[] getMonths() {
            return new String[]{"янв", "фев", "мар", "апр", "мая", "июн",
                    "июл", "авг", "сен", "окт", "нояб", "дек"};
        }
    };

    public String getDate() {
        long dv = (long) this.date * 1000;
        Date df = new java.util.Date(dv);
        SimpleDateFormat dd = new SimpleDateFormat("dd MMMM yyyy", myDateFormatSymbols);

        return dd.format(df);
    }

    public Integer getIntDate() {
        return date;
    }

    public String getStatus() {
        int id = this.status;
        String result = "";

        if (id == 0) result = NEW;
        else if (id == 1) result = APPROVED;
        else if (id == 2) result = WRITTEN_OFF;
        else if (id == -1) result = CANCELED;

        return result;
    }
}
