package org.delcom.pam_p4_ifs23021.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.delcom.pam_p4_ifs23021.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23021.ui.viewmodels.DestinationViewModel
import org.delcom.pam_p4_ifs23021.ui.viewmodels.PlantViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val plantViewModel: PlantViewModel by viewModels()

    // ✅ TAMBAHKAN INI
    private val destinationViewModel: DestinationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DelcomTheme {
                UIApp(
                    plantViewModel = plantViewModel,
                    destinationViewModel = destinationViewModel
                )
            }
        }
    }
}