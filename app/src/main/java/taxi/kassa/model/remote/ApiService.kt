package taxi.kassa.model.remote

import io.reactivex.Observable
import retrofit2.http.*
import taxi.kassa.model.responses.*

interface ApiService {

    @POST("auth")
    @FormUrlEncoded
    fun authSendPhone(@Field("phone") phone: String?): Observable<ResponseAPI<ResponseAuthSendPhone?>?>?

    @POST("auth")
    @FormUrlEncoded
    fun getCode(@Field("phone") phone: String?, @Field("code") code: String?): Observable<ResponseAPI<ResponseAuthSendCode?>?>?

    @POST("requests")
    @FormUrlEncoded
    fun createRequest(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("source_id") source_id: Int,
        @Field("key") key: String?
    ): Observable<ResponseAPI<ResponseCreateRequest?>?>?

    @GET("owner")
    fun getOwner(): Observable<ResponseAPI<ResponseOwner?>?>?

    @GET("withdrawals")
    fun getWithdraws(): Observable<ResponseAPI<Withdraws?>?>?

    @POST("withdrawals")
    @FormUrlEncoded
    fun createWithdraw(
        @Field("source_id") source_id: Int,
        @Field("amount") amount: String?,
        @Field("account_id") account_id: Int
    ): Observable<ResponseAPI<ResponseSimple?>?>?

    @GET("accounts")
    fun getAccounts(): Observable<ResponseAPI<AccountsList?>?>?

    @POST("accounts")
    @FormUrlEncoded
    fun createAccount(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("middle_name") middle_name: String?,
        @Field("account_number") account_number: String?,
        @Field("bank_code") bank_code: String?
    ): Observable<ResponseAPI<ResponseSimple?>?>?

    @POST("account_delete")
    @FormUrlEncoded
    fun deleteAccount(@Field("account_id") account_id: Int): Observable<ResponseAPI<ResponseSimple?>?>?

    @GET("orders")
    fun getOrders(@Query("offset") offset: String?): Observable<ResponseAPI<Orders?>?>?
}