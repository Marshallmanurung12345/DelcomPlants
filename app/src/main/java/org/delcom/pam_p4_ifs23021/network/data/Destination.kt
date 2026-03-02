package org.delcom.pam_p4_ifs23021.network.data

data class Destination(
    val id: String,
    val nama: String,
    val slug: String,
    val kategori: String,
    val deskripsi: String,
    val lokasi: String,
    val latitude: String? = null,
    val longitude: String? = null,
    val hargaTiket: String? = null,
    val jamBuka: String? = null,
    val kontak: String? = null,
    val pathGambar: String? = null,
    val createdAt: String,
    val updatedAt: String
)

data class ResponseDestinations(
    val destinations: List<Destination>
)

data class ResponseDestination(
    val destination: Destination
)

data class ResponseDestinationAdd(
    val destinationId: String
)