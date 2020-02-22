package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Withdraws {

    @SerializedName("info")
    private List<Withdraw> info = null;

    public List<Withdraw> getInfo() {
        return info;
    }

    public int getCount() {
        return info.size();
    }
}
