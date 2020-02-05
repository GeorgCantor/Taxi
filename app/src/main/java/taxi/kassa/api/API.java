package taxi.kassa.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import taxi.kassa.model.responses.AccountsList;
import taxi.kassa.model.responses.ResponseAPI;
import taxi.kassa.model.responses.ResponseCreateRequest;
import taxi.kassa.model.responses.ResponseOwner;
import taxi.kassa.model.responses.ResponseAuthSendCode;
import taxi.kassa.model.responses.ResponseAuthSendPhone;
import taxi.kassa.model.responses.ResponseSimple;
import taxi.kassa.model.responses.Withdraws;

public interface API {

    // auth

    @POST("auth")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseAuthSendPhone>> authSendPhone(@Field("phone") String phone);

    @POST("auth")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseAuthSendCode>> getCode(@Field("phone") String phone, @Field("code") String code);

    // requests

    @POST("requests")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseCreateRequest>> createRequest(@Field("name") String name, @Field("phone") String phone, @Field("source_id") int source_id, @Field("key") String key);

    // owner

    @GET("owner")
    Call<ResponseAPI<ResponseOwner>> getOwner();

    // withdrawal

    @GET("withdrawals")
    Call<ResponseAPI<Withdraws>> getWithdraws();

    @POST("withdrawals")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseSimple>> createWithdraw(@Field("source_id") int source_id,
                                                     @Field("amount") String amount,
                                                     @Field("account_id") int account_id);

    // accounts

    @GET("accounts")
    Call<ResponseAPI<AccountsList>> getAccounts();

    @POST("accounts")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseSimple>> createAccount(@Field("first_name") String first_name,
                                                    @Field("last_name") String last_name,
                                                    @Field("middle_name") String middle_name,
                                                    @Field("account_number") String account_number,
                                                    @Field("bank_code") String bank_code);

    @POST("account_delete")
    @FormUrlEncoded
    Call<ResponseAPI<ResponseSimple>> deleteAccount(@Field("account_id") int account_id);

}