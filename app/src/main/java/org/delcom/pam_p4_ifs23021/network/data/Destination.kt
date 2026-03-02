package org.delcom.pam_p4_ifs23021.network.data

import com.google.gson.annotations.SerializedName

data class Destination(
    val id: Int,
    @SerializedName("nama_wisata")
    val namaWisata: String,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("lokasi")
    val lokasi: String,
    @SerializedName("kategori")
    val kategori: String,
    @SerializedName("harga_tiket")
    val hargaTiket: Int,
    @SerializedName("jam_buka")
    val jamBuka: String
)
