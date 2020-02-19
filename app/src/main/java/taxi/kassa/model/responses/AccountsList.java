package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountsList {

    @SerializedName("info")
    private List<Account> info = null;

    public List<Account> getInfo() {
        return info;
    }

    public int getCount() {
        return info.size();
    }
}