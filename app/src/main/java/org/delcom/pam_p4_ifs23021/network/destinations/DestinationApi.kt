package org.delcom.pam_p4_ifs23021.network.destinations

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestination
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinationAdd
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinations
import org.delcom.pam_p4_ifs23021.network.data.ResponseMessage
import retrofit2.http.*

interface DestinationApi {

    @GET("destinations")
    suspend fun getAll(
        @Query("search") search: String? = null,
        @Query("kategori") kategori: String? = null
    ): ResponseMessage<ResponseDestinations?>

    @GET("destinations/{id}")
    suspend fun getById(
        @Path("id") id: String
    ): ResponseMessage<ResponseDestination?>

    @Multipart
    @POST("destinations")
    suspend fun create(
        @Part("nama") nama: RequestBody,
        @Part("slug") slug: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("hargaTiket") hargaTiket: RequestBody,
        @Part("jamBuka") jamBuka: RequestBody,
        @Part("kontak") kontak: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<ResponseDestinationAdd?>

    @Multipart
    @PUT("destinations/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Part("nama") nama: RequestBody,
        @Part("slug") slug: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("lokasi") lokasi: RequestBody,
        @Part("hargaTiket") hargaTiket: RequestBody,
        @Part("jamBuka") jamBuka: RequestBody,
        @Part("kontak") kontak: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    @DELETE("destinations/{id}")
    suspend fun delete(
        @Path("id") id: String
    ): ResponseMessage<String?>
}