package taxi.kassa.model.responses;

public class Account {

    private int id;
    private String account_number;
    private String account_corr;
    private String bank_name;
    private String driver_name;
    private String card_name;
    private Boolean readonly;

    public Account(int id, String account_number, String account_corr, String bank_name, String driver_name, String card_name, Boolean readonly) {
        this.id = id;
        this.account_number = account_number;
        this.account_corr = account_corr;
        this.bank_name = bank_name;
        this.driver_name = driver_name;
        this.card_name = card_name;
        this.readonly = readonly;
    }

    public int getId() {
        return this.id;
    }

    public String getAccountNumber() {
        return this.account_number;
    }

    public String getAccountCorr() {
        return this.account_corr;
    }

    public String getBankName() {
        return this.bank_name;
    }

    public String getDriverName() {
        return this.driver_name;
    }

    public String getCardName() {
        return this.card_name;
    }

    public Boolean getReadonly() {
        return this.readonly;
    }
}
