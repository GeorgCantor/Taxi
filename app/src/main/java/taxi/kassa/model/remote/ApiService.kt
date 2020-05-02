package taxi.kassa.model.remote

import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.*
import taxi.kassa.model.responses.*

interface ApiService {

    @POST("auth")
    @FormUrlEncoded
    fun authSendPhone(@Field("phone") phone: String?): Observable<ResponseAPI<ResponseAuthSendPhone?>?>?

    @POST("auth")
    @FormUrlEncoded
    fun getCode(
        @Field("phone") phone: String?,
        @Field("code") code: String?
    ): Observable<ResponseAPI<ResponseAuthSendCode?>?>?

    @POST("requests")
    @FormUrlEncoded
    fun createRequest(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("source_id") source_id: Int,
        @Field("key") key: String?
    ): Observable<ResponseAPI<ResponseCreateRequest?>?>?

    @GET("owner")
    fun getOwnerAsync(): Deferred<ResponseAPI<ResponseOwner?>?>?

    @GET("withdrawals")
    fun getWithdrawsAsync(): Deferred<ResponseAPI<Withdraws?>?>?

    @POST("withdrawals")
    @FormUrlEncoded
    fun createWithdrawAsync(
        @Field("source_id") source_id: Int,
        @Field("amount") amount: String?,
        @Field("account_id") account_id: Int
    ): Deferred<ResponseAPI<ResponseSimple?>?>?

    @GET("accounts")
    fun getAccountsAsync(): Deferred<ResponseAPI<AccountsList?>?>?

    @POST("accounts")
    @FormUrlEncoded
    fun createAccountAsync(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("middle_name") middle_name: String?,
        @Field("account_number") account_number: String?,
        @Field("bank_code") bank_code: String?
    ): Deferred<ResponseAPI<ResponseSimple?>?>?

    @POST("account_delete")
    @FormUrlEncoded
    fun deleteAccountAsync(@Field("account_id") account_id: Int): Deferred<ResponseAPI<ResponseSimple?>?>?

    @GET("orders")
    fun getOrdersAsync(@Query("offset") offset: String?): Deferred<ResponseAPI<Orders?>?>?
}