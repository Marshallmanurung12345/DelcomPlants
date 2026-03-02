package org.delcom.pam_p4_ifs23021.network.destinations

import org.delcom.pam_p4_ifs23021.network.data.*
import retrofit2.http.*

interface DestinationApi {
    @GET("destinations")
    suspend fun getAll(@Query("search") search: String = ""): DataResponse<List<Destination>>

    @GET("destinations/{id}")
    suspend fun getById(@Path("id") id: Int): DataResponse<Destination>

    @POST("destinations")
    suspend fun create(@Body req: DestinationRequest): DataResponse<Destination>

    @PUT("destinations/{id}")
    suspend fun update(@Path("id") id: Int, @Body req: DestinationRequest): DataResponse<Destination>

    @DELETE("destinations/{id}")
    suspend fun delete(@Path("id") id: Int): DataResponse<Map<String, Int>>
}