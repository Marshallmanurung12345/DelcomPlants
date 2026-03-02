package org.delcom.pam_p4_ifs23021.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23021.R
import org.delcom.pam_p4_ifs23021.helper.AlertHelper
import org.delcom.pam_p4_ifs23021.helper.AlertState
import org.delcom.pam_p4_ifs23021.helper.AlertType
import org.delcom.pam_p4_ifs23021.helper.ConstHelper
import org.delcom.pam_p4_ifs23021.helper.RouteHelper
import org.delcom.pam_p4_ifs23021.helper.SuspendHelper
import org.delcom.pam_p4_ifs23021.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23021.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23021.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23021.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23021.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationActionUIState
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationAddScreen(
    navController: NavHostController,
    destinationViewModel: DestinationViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by destinationViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    // LaunchedEffect(Unit) dihapus agar tidak me-reset state secara tidak sengaja saat komposisi ulang

    LaunchedEffect(uiState.destinationAction) {
        when (val state = uiState.destinationAction) {
            is DestinationActionUIState.Success -> {
                if (isLoading) { // Pastikan hanya bereaksi jika sedang dalam proses loading (simpan)
                    SuspendHelper.showSnackBar(
                        snackbarHostState, SuspendHelper.SnackBarType.SUCCESS, "Destinasi berhasil ditambahkan!"
                    )
                    isLoading = false
                    // Navigasi kembali ke daftar destinasi
                    RouteHelper.to(navController, ConstHelper.RouteNames.Destinations.path, true)
                }
            }
            is DestinationActionUIState.Error -> {
                if (isLoading) {
                    SuspendHelper.showSnackBar(
                        snackbarHostState, SuspendHelper.SnackBarType.ERROR, state.message
                    )
                    isLoading = false
                }
            }
            else -> {}
        }
    }

    if (isLoading) {
        LoadingUI()
        // Kita tidak mereturn di sini agar Scaffold tetap ada untuk menampilkan SnackBar jika terjadi error
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopAppBarComponent(
                navController = navController,
                title = "Tambah Destinasi",
                showBackButton = true
            )
            Box(modifier = Modifier.weight(1f)) {
                DestinationAddUI(
                    onSave = { nama, slug, kategori, deskripsi, lokasi, hargaTiket, jamBuka, kontak, fileUri ->
                        isLoading = true
                        val context = navController.context
                        val filePart = fileUri?.let { uriToMultipart(context, it, "file") }
                        destinationViewModel.postDestination(
                            nama = nama.toRequestBodyText(),
                            slug = slug.toRequestBodyText(),
                            kategori = kategori.toRequestBodyText(),
                            deskripsi = deskripsi.toRequestBodyText(),
                            lokasi = lokasi.toRequestBodyText(),
                            hargaTiket = hargaTiket.toRequestBodyText(),
                            jamBuka = jamBuka.toRequestBodyText(),
                            kontak = kontak.toRequestBodyText(),
                            file = filePart
                        )
                    }
                )
            }
            BottomNavComponent(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationAddUI(
    onSave: (
        nama: String,
        slug: String,
        kategori: String,
        deskripsi: String,
        lokasi: String,
        hargaTiket: String,
        jamBuka: String,
        kontak: String,
        file: Uri?
    ) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf("") }
    var dataSlug by remember { mutableStateOf("") }
    var dataKategori by remember { mutableStateOf("") }
    var dataDeskripsi by remember { mutableStateOf("") }
    var dataLokasi by remember { mutableStateOf("") }
    var dataHarga by remember { mutableStateOf("") }
    var dataJam by remember { mutableStateOf("") }
    var dataKontak by remember { mutableStateOf("") }

    val kategoriOptions = listOf("alam", "budaya", "religi", "kuliner")
    var expandedKategori by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> dataFile = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Image Picker
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (dataFile != null) {
                    AsyncImage(
                        model = dataFile,
                        contentDescription = "Pratinjau",
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Pilih Gambar", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Text("Tap untuk memilih gambar", style = MaterialTheme.typography.bodySmall)
        }

        DestinationTextField(value = dataNama, onValueChange = {
            dataNama = it
            dataSlug = it.lowercase().replace(" ", "-")
        }, label = "Nama Destinasi")

        DestinationTextField(value = dataSlug, onValueChange = { dataSlug = it }, label = "Slug")

        // Dropdown Kategori
        ExposedDropdownMenuBox(
            expanded = expandedKategori,
            onExpandedChange = { expandedKategori = it }
        ) {
            OutlinedTextField(
                value = dataKategori,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expandedKategori,
                onDismissRequest = { expandedKategori = false }
            ) {
                kategoriOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { dataKategori = option; expandedKategori = false }
                    )
                }
            }
        }

        DestinationTextField(
            value = dataDeskripsi, onValueChange = { dataDeskripsi = it },
            label = "Deskripsi", minLines = 3, maxLines = 5,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        DestinationTextField(value = dataLokasi, onValueChange = { dataLokasi = it }, label = "Lokasi")
        DestinationTextField(value = dataHarga, onValueChange = { dataHarga = it }, label = "Harga Tiket (Rp)", keyboardType = KeyboardType.Number)
        DestinationTextField(value = dataJam, onValueChange = { dataJam = it }, label = "Jam Buka (misal: 08.00 - 17.00 WIB)")
        DestinationTextField(value = dataKontak, onValueChange = { dataKontak = it }, label = "Kontak")

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                when {
                    dataNama.isEmpty() -> AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!")
                    dataKategori.isEmpty() -> AlertHelper.show(alertState, AlertType.ERROR, "Pilih kategori!")
                    dataDeskripsi.isEmpty() -> AlertHelper.show(alertState, AlertType.ERROR, "Deskripsi tidak boleh kosong!")
                    dataLokasi.isEmpty() -> AlertHelper.show(alertState, AlertType.ERROR, "Lokasi tidak boleh kosong!")
                    else -> onSave(dataNama, dataSlug, dataKategori, dataDeskripsi, dataLokasi, dataHarga, dataJam, dataKontak, dataFile)
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = { AlertHelper.dismiss(alertState) },
            title = { Text(alertState.value.type.title) },
            text = { Text(alertState.value.message) },
            confirmButton = {
                TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") }
            }
        )
    }
}

@Composable
fun DestinationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
        minLines = minLines,
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    )
}