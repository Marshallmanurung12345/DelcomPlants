package org.delcom.pam_p4_ifs23021.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel

@Composable
fun DestinationDetailScreen(
    navController: NavHostController,
    destinationId: String,
    destinationViewModel: DestinationViewModel
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detail Destinasi", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("ID: $destinationId")
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = { navController.popBackStack() }) { Text("Kembali") }
    }
}