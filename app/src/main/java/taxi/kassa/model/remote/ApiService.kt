package taxi.kassa.model.remote

import okhttp3.MultipartBody
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

    @POST("fuel_fill")
    @FormUrlEncoded
    suspend fun refillFuelBalance(
        @Field("agregator") aggregator: String,
        @Field("amount") amount: Float
    ): ResponseAPI<ResponseFuelBalance?>?

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

    @GET("cards")
    suspend fun getCards(): ResponseAPI<Cards?>?

    @POST("card_add")
    @FormUrlEncoded
    suspend fun addCard(@Field("card_number") card_number: String): ResponseAPI<ResponseAddCard?>?

    @POST("card_delete")
    @FormUrlEncoded
    suspend fun deleteCard(@Field("card_id") card_id: Int): ResponseAPI<ResponseSimple?>?

    @GET("orders")
    suspend fun getOrders(@Query("offset") offset: String?): ResponseAPI<Orders?>?

    @GET("messages")
    suspend fun getMessages(@Query("offset") offset: String?): ResponseAPI<Messages?>?

    @POST("message_create")
    @FormUrlEncoded
    suspend fun sendMessage(@Field("text") text: String): ResponseAPI<ResponseMessageCreate?>?

    @POST("requests")
    @FormUrlEncoded
    suspend fun sendRegisterRequest(
        @Field("source_id") source_id: Int,
        @Field("phone") phone: String,
        @Field("key") key: String,
        @Field("request_uid") request_uid: String,
        @Field("gett_id") gett_id: String
    ): ResponseAPI<ResponseRegisterRequest?>?

    @Multipart
    @POST("request_file")
    suspend fun sendPhoto(
        @Part file: MultipartBody.Part,
        @Part("request_uid") request_uid: String,
        @Part("type") type: Int
    ): ResponseAPI<ResponseSimple?>?
}