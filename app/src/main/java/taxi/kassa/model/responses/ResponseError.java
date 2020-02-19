package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ResponseError {

    @SerializedName("error_msg")
    private String error_msg = null;
    @SerializedName("error_data")
    private HashMap<String, String> error_data = null;

    String getErrorMsg() {
        return error_msg;
    }

    HashMap<String, String> getErrorData() {
        return error_data;
    }
}
