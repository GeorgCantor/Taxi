package taxi.kassa.model.responses;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;

public class ResponseAPI<T> {
    // vars
    private ResponseError error;
    @SerializedName("success")
    private boolean success = Boolean.parseBoolean(null);
    @SerializedName("response")
    private T response = null;
    // methods
    public T getResponse() {return response;}
    public boolean getSuccess(){return success;}
    public String getErrorMsg(){return error.getErrorMsg();}
    public HashMap<String, String> getErrorData(){return error.getErrorData();}

}
