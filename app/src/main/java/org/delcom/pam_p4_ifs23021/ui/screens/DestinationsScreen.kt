package org.delcom.pam_p4_ifs23021.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23021.R
import org.delcom.pam_p4_ifs23021.helper.ConstHelper
import org.delcom.pam_p4_ifs23021.helper.RouteHelper
import org.delcom.pam_p4_ifs23021.helper.ToolsHelper
import org.delcom.pam_p4_ifs23021.network.data.Destination
import org.delcom.pam_p4_ifs23021.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23021.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23021.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationsUIState

@Composable
fun DestinationsScreen(
    navController: NavHostController,
    destinationViewModel: DestinationViewModel
) {
    val uiState by destinationViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var destinations by remember { mutableStateOf<List<Destination>>(emptyList()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    fun fetchData() {
        isLoading = true
        destinationViewModel.getAllDestinations(searchQuery.text)
    }

    LaunchedEffect(Unit) { fetchData() }

    LaunchedEffect(uiState.destinations) {
        if (uiState.destinations !is DestinationsUIState.Loading) {
            isLoading = false
            destinations = if (uiState.destinations is DestinationsUIState.Success) {
                (uiState.destinations as DestinationsUIState.Success).data
            } else {
                emptyList()
            }
        }
    }

    if (isLoading) {
        LoadingUI()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Wisata Samosir",
            showBackButton = false,
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchAction = { fetchData() }
        )

        Box(modifier = Modifier.weight(1f)) {
            DestinationsUI(
                destinations = destinations,
                onOpen = { id ->
                    RouteHelper.to(navController, "destinations/$id")
                }
            )

            FloatingActionButton(
                onClick = {
                    RouteHelper.to(navController, ConstHelper.RouteNames.DestinationsAdd.path)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Destinasi")
            }
        }

        BottomNavComponent(navController = navController)
    }
}

@Composable
fun DestinationsUI(
    destinations: List<Destination>,
    onOpen: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(destinations) { destination ->
            DestinationItemUI(destination = destination, onOpen = onOpen)
        }
    }

    if (destinations.isEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Tidak ada data destinasi!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun DestinationItemUI(
    destination: Destination,
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOpen(destination.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = ToolsHelper.getDestinationImageUrl(destination.id),
                contentDescription = destination.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = destination.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "📍 ${destination.lokasi}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = destination.kategori,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    destination.hargaTiket?.let { harga ->
                        if (harga.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "Rp ${harga}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}