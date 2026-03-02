package org.delcom.pam_p4_ifs23021.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23021.helper.ConstHelper
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@Composable
fun DestinationsScreen(
    navController: NavHostController,
    destinationViewModel: DestinationViewModel
) {
    val state by destinationViewModel.state.collectAsState()

    LaunchedEffect(Unit) { destinationViewModel.fetch() }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Wisata Samosir", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { navController.navigate(ConstHelper.RouteNames.DestinationsAdd.path) }) {
                Text("Tambah")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (state.loading) {
            CircularProgressIndicator()
            return@Column
        }

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        LazyColumn {
            items(state.data) { d ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(d.namaWisata, style = MaterialTheme.typography.titleMedium)
                        Text(d.lokasi, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = {
                                navController.navigate("destinations/${d.id}")
                            }) { Text("Detail") }

                            TextButton(onClick = {
                                navController.navigate("destinations/${d.id}/edit")
                            }) { Text("Edit") }

                            TextButton(onClick = {
                                destinationViewModel.delete(d.id)
                            }) { Text("Hapus") }
                        }
                    }
                }
            }
        }
    }
}