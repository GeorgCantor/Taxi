package taxi.kassa.model.remote

import retrofit2.http.*
import taxi.kassa.model.responses.*

interface ApiService {

    @POST("auth")
    @FormUrlEncoded
    suspend fun authSendPhone(@Field("phone") phone: String?): ResponseAPI<ResponseAuthSendPhone?>?

    @POST("auth")
    @FormUrlEncoded
    suspend fun getCode(
        @Field("phone") phone: String?,
        @Field("code") code: String?
    ): ResponseAPI<ResponseAuthSendCode?>?

    @POST("requests")
    @FormUrlEncoded
    suspend fun createRequest(
        @Field("name") name: String?,
        @Field("phone") phone: String?,
        @Field("source_id") source_id: Int,
        @Field("key") key: String?
    ): ResponseAPI<ResponseCreateRequest?>?

    @GET("owner")
    suspend fun getOwner(): ResponseAPI<ResponseOwner?>?

    @GET("withdrawals")
    suspend fun getWithdraws(): ResponseAPI<Withdraws?>?

    @POST("withdrawals")
    @FormUrlEncoded
    suspend fun createWithdraw(
        @Field("source_id") source_id: Int,
        @Field("amount") amount: String?,
        @Field("account_id") account_id: Int
    ): ResponseAPI<ResponseSimple?>?

    @GET("accounts")
    suspend fun getAccounts(): ResponseAPI<AccountsList?>?

    @POST("accounts")
    @FormUrlEncoded
    suspend fun createAccount(
        @Field("first_name") first_name: String?,
        @Field("last_name") last_name: String?,
        @Field("middle_name") middle_name: String?,
        @Field("account_number") account_number: String?,
        @Field("bank_code") bank_code: String?
    ): ResponseAPI<ResponseSimple?>?

    @POST("account_delete")
    @FormUrlEncoded
    suspend fun deleteAccount(@Field("account_id") account_id: Int): ResponseAPI<ResponseSimple?>?

    @GET("orders")
    suspend fun getOrders(@Query("offset") offset: String?): ResponseAPI<Orders?>?

    @GET("messages")
    suspend fun getMessages(@Query("offset") offset: String?): ResponseAPI<Messages?>?
}