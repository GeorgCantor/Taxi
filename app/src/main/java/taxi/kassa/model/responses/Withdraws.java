package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Withdraws {
    // vars
    @SerializedName("info")
    private List<Withdraw> info = null;
    // methods
    public List<Withdraw> getInfo() {
        return info;
    }
    public int getCount() {
        return info.size();
    }
}
