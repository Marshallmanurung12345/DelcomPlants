package org.delcom.pam_p4_ifs23021.network.destinations

import javax.inject.Inject
import org.delcom.pam_p4_ifs23021.network.data.DestinationRequest

class DestinationRepository @Inject constructor(
    private val api: DestinationApi
) {
    suspend fun getAll(search: String) = api.getAll(search)
    suspend fun getById(id: Int) = api.getById(id)
    suspend fun create(req: DestinationRequest) = api.create(req)
    suspend fun update(id: Int, req: DestinationRequest) = api.update(id, req)
    suspend fun delete(id: Int) = api.delete(id)
}