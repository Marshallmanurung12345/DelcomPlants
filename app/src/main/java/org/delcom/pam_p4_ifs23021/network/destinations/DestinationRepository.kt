package org.delcom.pam_p4_ifs23021.network.destinations

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23021.helper.SuspendHelper
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestination
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinationAdd
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinations
import org.delcom.pam_p4_ifs23021.network.data.ResponseMessage
import javax.inject.Inject

class DestinationRepository @Inject constructor(
    private val api: DestinationApi
) : IDestinationRepository {

    override suspend fun getAllDestinations(
        search: String?,
        kategori: String?
    ): ResponseMessage<ResponseDestinations?> {
        return SuspendHelper.safeApiCall { api.getAll(search, kategori) }
    }

    override suspend fun getDestinationById(id: String): ResponseMessage<ResponseDestination?> {
        return SuspendHelper.safeApiCall { api.getById(id) }
    }

    override suspend fun postDestination(
        nama: RequestBody,
        slug: RequestBody,
        kategori: RequestBody,
        deskripsi: RequestBody,
        lokasi: RequestBody,
        hargaTiket: RequestBody,
        jamBuka: RequestBody,
        kontak: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<ResponseDestinationAdd?> {
        return SuspendHelper.safeApiCall {
            api.create(nama, slug, kategori, deskripsi, lokasi, hargaTiket, jamBuka, kontak, file)
        }
    }

    override suspend fun putDestination(
        id: String,
        nama: RequestBody,
        slug: RequestBody,
        kategori: RequestBody,
        deskripsi: RequestBody,
        lokasi: RequestBody,
        hargaTiket: RequestBody,
        jamBuka: RequestBody,
        kontak: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            api.update(id, nama, slug, kategori, deskripsi, lokasi, hargaTiket, jamBuka, kontak, file)
        }
    }

    override suspend fun deleteDestination(id: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall { api.delete(id) }
    }
}