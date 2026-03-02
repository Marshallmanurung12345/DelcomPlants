package org.delcom.pam_p4_ifs23021.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23021.network.data.Destination
import org.delcom.pam_p4_ifs23021.network.destinations.IDestinationRepository
import javax.inject.Inject

// ===== UI States =====
sealed interface DestinationsUIState {
    data class Success(val data: List<Destination>) : DestinationsUIState
    data class Error(val message: String) : DestinationsUIState
    object Loading : DestinationsUIState
}

sealed interface DestinationUIState {
    data class Success(val data: Destination) : DestinationUIState
    data class Error(val message: String) : DestinationUIState
    object Loading : DestinationUIState
}

sealed interface DestinationActionUIState {
    data class Success(val message: String) : DestinationActionUIState
    data class Error(val message: String) : DestinationActionUIState
    object Loading : DestinationActionUIState
}

data class UIStateDestination(
    val destinations: DestinationsUIState = DestinationsUIState.Loading,
    var destination: DestinationUIState = DestinationUIState.Loading,
    var destinationAction: DestinationActionUIState = DestinationActionUIState.Loading
)

@HiltViewModel
@Keep
class DestinationViewModel @Inject constructor(
    private val repository: IDestinationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIStateDestination())
    val uiState = _uiState.asStateFlow()

    fun getAllDestinations(search: String? = null, kategori: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(destinations = DestinationsUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.getAllDestinations(search, kategori)
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") {
                            DestinationsUIState.Success(res.data?.destinations ?: emptyList())
                        } else {
                            DestinationsUIState.Error(res.message)
                        }
                    },
                    onFailure = { e ->
                        DestinationsUIState.Error(e.message ?: "Unknown error")
                    }
                )
                it.copy(destinations = state)
            }
        }
    }

    fun getDestinationById(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(destination = DestinationUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.getDestinationById(id)
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success" && res.data?.destination != null) {
                            DestinationUIState.Success(res.data.destination)
                        } else {
                            DestinationUIState.Error(res.message)
                        }
                    },
                    onFailure = { e ->
                        DestinationUIState.Error(e.message ?: "Unknown error")
                    }
                )
                it.copy(destination = state)
            }
        }
    }

    fun postDestination(
        nama: RequestBody,
        slug: RequestBody,
        kategori: RequestBody,
        deskripsi: RequestBody,
        lokasi: RequestBody,
        hargaTiket: RequestBody,
        jamBuka: RequestBody,
        kontak: RequestBody,
        file: MultipartBody.Part? = null
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(destinationAction = DestinationActionUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.postDestination(
                        nama, slug, kategori, deskripsi, lokasi, hargaTiket, jamBuka, kontak, file
                    )
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") {
                            DestinationActionUIState.Success(res.data?.destinationId ?: res.message)
                        } else {
                            DestinationActionUIState.Error(res.message)
                        }
                    },
                    onFailure = { e ->
                        DestinationActionUIState.Error(e.message ?: "Unknown error")
                    }
                )
                it.copy(destinationAction = state)
            }
        }
    }

    fun putDestination(
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
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(destinationAction = DestinationActionUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.putDestination(
                        id, nama, slug, kategori, deskripsi, lokasi, hargaTiket, jamBuka, kontak, file
                    )
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") {
                            DestinationActionUIState.Success(res.message)
                        } else {
                            DestinationActionUIState.Error(res.message)
                        }
                    },
                    onFailure = { e ->
                        DestinationActionUIState.Error(e.message ?: "Unknown error")
                    }
                )
                it.copy(destinationAction = state)
            }
        }
    }

    fun deleteDestination(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(destinationAction = DestinationActionUIState.Loading) }
            _uiState.update {
                val state = runCatching {
                    repository.deleteDestination(id)
                }.fold(
                    onSuccess = { res ->
                        if (res.status == "success") {
                            DestinationActionUIState.Success(res.message)
                        } else {
                            DestinationActionUIState.Error(res.message)
                        }
                    },
                    onFailure = { e ->
                        DestinationActionUIState.Error(e.message ?: "Unknown error")
                    }
                )
                it.copy(destinationAction = state)
            }
        }
    }
}