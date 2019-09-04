package taxi.kassa.responses;

public class ResponseOwner {
    // vars
    String balance_city;
    String balance_gett;
    String balance_yandex;
    String balance_total;
    String first_name;
    String last_name;
    String phone;
    // methods
    public String getBalanceCity(){return balance_city;}
    public String getBalanceGett(){return balance_gett;}
    public String getBalanceYandex(){return balance_yandex;}
    public String getBalanceTotal(){return balance_total;}
    public String getFullName(){return last_name + " " + first_name;}
    public String getPhone(){return phone;}
}

