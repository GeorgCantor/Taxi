package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AccountsList {
    // vars
    @SerializedName("info")
    private List<Account> info = null;
    // methods
    public List<Account> getInfo() {return info;}
    public int getCount() {return info.size();}
}