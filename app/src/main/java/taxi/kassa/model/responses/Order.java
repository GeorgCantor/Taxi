package taxi.kassa.model.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("source_id")
    @Expose
    private String sourceId;
    @SerializedName("address_from")
    @Expose
    private String addressFrom;
    @SerializedName("address_to")
    @Expose
    private String addressTo;
    @SerializedName("amount_client")
    @Expose
    private String amountClient;
    @SerializedName("amount_driver")
    @Expose
    private String amountDriver;
    @SerializedName("amount_tip")
    @Expose
    private String amountTip;
    @SerializedName("amount_fee_agr")
    @Expose
    private String amountFeeAgr;
    @SerializedName("amount_fee_our")
    @Expose
    private String amountFeeOur;
    @SerializedName("amount_total")
    @Expose
    private Float amountTotal;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("statunt_total")
    @Expose
    private Float statuntTotal;
    @SerializedName("ant_tip")
    @Expose
    private String antTip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getAmountClient() {
        return amountClient;
    }

    public void setAmountClient(String amountClient) {
        this.amountClient = amountClient;
    }

    public String getAmountDriver() {
        return amountDriver;
    }

    public void setAmountDriver(String amountDriver) {
        this.amountDriver = amountDriver;
    }

    public String getAmountTip() {
        return amountTip;
    }

    public void setAmountTip(String amountTip) {
        this.amountTip = amountTip;
    }

    public String getAmountFeeAgr() {
        return amountFeeAgr;
    }

    public void setAmountFeeAgr(String amountFeeAgr) {
        this.amountFeeAgr = amountFeeAgr;
    }

    public String getAmountFeeOur() {
        return amountFeeOur;
    }

    public void setAmountFeeOur(String amountFeeOur) {
        this.amountFeeOur = amountFeeOur;
    }

    public Float getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(Float amountTotal) {
        this.amountTotal = amountTotal;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Float getStatuntTotal() {
        return statuntTotal;
    }

    public void setStatuntTotal(Float statuntTotal) {
        this.statuntTotal = statuntTotal;
    }

    public String getAntTip() {
        return antTip;
    }

    public void setAntTip(String antTip) {
        this.antTip = antTip;
    }

}
