package org.delcom.pam_p4_ifs23021.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.delcom.pam_p4_ifs23021.network.data.*
import org.delcom.pam_p4_ifs23021.network.destinations.DestinationRepository
import javax.inject.Inject
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel
data class DestinationState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<Destination> = emptyList()
)

@HiltViewModel
class DestinationViewModel @Inject constructor(
    private val repo: DestinationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DestinationState())
    val state: StateFlow<DestinationState> = _state.asStateFlow()

    fun fetch(search: String = "") {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val res = repo.getAll(search)
                _state.update { it.copy(loading = false, data = res.data ?: emptyList()) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "Gagal memuat data") }
            }
        }
    }

    fun create(req: DestinationRequest, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                repo.create(req)
                fetch()
                onDone()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Gagal menambah data") }
            }
        }
    }

    fun update(id: Int, req: DestinationRequest, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                repo.update(id, req)
                fetch()
                onDone()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Gagal mengubah data") }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                repo.delete(id)
                fetch()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Gagal menghapus data") }
            }
        }
    }
}