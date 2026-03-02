package org.delcom.pam_p4_ifs23021.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23021.network.data.DestinationRequest
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@Composable
fun DestinationAddScreen(
    navController: NavHostController,
    destinationViewModel: DestinationViewModel
) {
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tambah Destinasi", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(nama, { nama = it }, label = { Text("Nama Wisata") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(deskripsi, { deskripsi = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(lokasi, { lokasi = it }, label = { Text("Lokasi") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(kategori, { kategori = it }, label = { Text("Kategori") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(harga, { harga = it }, label = { Text("Harga Tiket") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(jam, { jam = it }, label = { Text("Jam Buka") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { navController.popBackStack() }) { Text("Batal") }
            Button(onClick = {
                destinationViewModel.create(
                    DestinationRequest(
                        namaWisata = nama,
                        deskripsi = deskripsi,
                        lokasi = lokasi,
                        kategori = kategori,
                        hargaTiket = harga, // Dikirim sebagai String
                        jamBuka = jam
                    )
                ) { navController.popBackStack() }
            }) { Text("Simpan") }
        }
    }
}
