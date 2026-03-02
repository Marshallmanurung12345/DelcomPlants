package org.delcom.pam_p4_ifs23021.network.data

data class DataResponse<T>(
    val status: String,
    val message: String,
    val data: T? = null
)