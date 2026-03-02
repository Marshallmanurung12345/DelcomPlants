package org.delcom.pam_p4_ifs23021.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23021.R
import org.delcom.pam_p4_ifs23021.helper.ConstHelper
import org.delcom.pam_p4_ifs23021.helper.RouteHelper
import org.delcom.pam_p4_ifs23021.helper.SuspendHelper
import org.delcom.pam_p4_ifs23021.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23021.helper.ToolsHelper
import org.delcom.pam_p4_ifs23021.network.data.Destination
import org.delcom.pam_p4_ifs23021.ui.components.*
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationActionUIState
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationUIState
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@Composable
fun DestinationDetailScreen(
    navController: NavHostController,
    destinationId: String,
    destinationViewModel: DestinationViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by destinationViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
    var destination by remember { mutableStateOf<Destination?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiState.destinationAction = DestinationActionUIState.Loading
        uiState.destination = DestinationUIState.Loading
        destinationViewModel.getDestinationById(destinationId)
    }

    LaunchedEffect(uiState.destination) {
        if (uiState.destination !is DestinationUIState.Loading) {
            isLoading = false
            if (uiState.destination is DestinationUIState.Success) {
                destination = (uiState.destination as DestinationUIState.Success).data
            } else {
                RouteHelper.back(navController)
            }
        }
    }

    LaunchedEffect(uiState.destinationAction) {
        when (val state = uiState.destinationAction) {
            is DestinationActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHostState, SnackBarType.SUCCESS, state.message)
                RouteHelper.to(navController, ConstHelper.RouteNames.Destinations.path, true)
                isLoading = false
            }
            is DestinationActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHostState, SnackBarType.ERROR, state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if (isLoading || destination == null) {
        LoadingUI()
        return
    }

    val menuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.DestinationsEdit.path
                        .replace("{destinationId}", destination!!.id)
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            isDestructive = true,
            onClick = { isConfirmDelete = true }
        )
    )

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopAppBarComponent(
                navController = navController,
                title = destination!!.nama,
                showBackButton = true,
                customMenuItems = menuItems
            )
            Box(modifier = Modifier.weight(1f)) {
                DestinationDetailUI(destination = destination!!)
                BottomDialog(
                    type = BottomDialogType.ERROR,
                    show = isConfirmDelete,
                    onDismiss = { isConfirmDelete = false },
                    title = "Konfirmasi Hapus",
                    message = "Apakah Anda yakin ingin menghapus destinasi \"${destination!!.nama}\"?",
                    confirmText = "Ya, Hapus",
                    onConfirm = {
                        isLoading = true
                        destinationViewModel.deleteDestination(destinationId)
                    },
                    cancelText = "Batal",
                    destructiveAction = true
                )
            }
            BottomNavComponent(navController = navController)
        }
    }
}

@Composable
fun DestinationDetailUI(destination: Destination) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Gambar
        AsyncImage(
            model = ToolsHelper.getDestinationImageUrl(destination.id),
            contentDescription = destination.nama,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = destination.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = destination.kategori,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Cards
            InfoDetailCard(label = "📍 Lokasi", value = destination.lokasi)

            destination.hargaTiket?.let {
                if (it.isNotEmpty()) InfoDetailCard(label = "🎟️ Harga Tiket", value = "Rp $it")
            }
            destination.jamBuka?.let {
                if (it.isNotEmpty()) InfoDetailCard(label = "🕐 Jam Buka", value = it)
            }
            destination.kontak?.let {
                if (it.isNotEmpty()) InfoDetailCard(label = "📞 Kontak", value = it)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Deskripsi
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Deskripsi",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
                    Text(
                        text = destination.deskripsi,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoDetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}