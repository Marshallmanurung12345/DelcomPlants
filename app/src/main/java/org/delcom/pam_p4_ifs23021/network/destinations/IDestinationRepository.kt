package org.delcom.pam_p4_ifs23021.network.destinations

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestination
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinationAdd
import org.delcom.pam_p4_ifs23021.network.data.ResponseDestinations
import org.delcom.pam_p4_ifs23021.network.data.ResponseMessage

interface IDestinationRepository {

    suspend fun getAllDestinations(
        search: String? = null,
        kategori: String? = null
    ): ResponseMessage<ResponseDestinations?>

    suspend fun getDestinationById(
        id: String
    ): ResponseMessage<ResponseDestination?>

    suspend fun postDestination(
        nama: RequestBody,
        slug: RequestBody,
        kategori: RequestBody,
        deskripsi: RequestBody,
        lokasi: RequestBody,
        hargaTiket: RequestBody,
        jamBuka: RequestBody,
        kontak: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<ResponseDestinationAdd?>

    suspend fun putDestination(
        id: String,
        nama: RequestBody,
        slug: RequestBody,
        kategori: RequestBody,
        deskripsi: RequestBody,
        lokasi: RequestBody,
        hargaTiket: RequestBody,
        jamBuka: RequestBody,
        kontak: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    suspend fun deleteDestination(
        id: String
    ): ResponseMessage<String?>
}