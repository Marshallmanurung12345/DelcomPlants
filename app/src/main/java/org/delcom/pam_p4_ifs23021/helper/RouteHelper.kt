
package org.delcom.pam_p4_ifs23021.helper

import androidx.navigation.NavHostController

object RouteHelper {
    fun to(
        navController: NavHostController,
        destination: String,
        removeBackStack: Boolean = false,
        popUpTo: String? = null,
    ) {
        if (removeBackStack) {
            navController.navigate(destination) {
                if(popUpTo == null){
                    popUpTo(0) { inclusive = true } // hapus semua stack
                }else{
                    popUpTo(popUpTo) { inclusive = true }
                }
                launchSingleTop = true
            }
        } else {
            navController.navigate(destination) {
                launchSingleTop = true
            }
        }
    }
    object RouteHelper {
        const val HOME = "home"
        const val PLANTS = "plants"
        const val PROFILE = "profile"

        // ✅ Wisata Samosir
        const val DESTINATIONS = "destinations"
        const val DESTINATION_ADD = "destination_add"
        const val DESTINATION_DETAIL = "destination_detail"
        const val DESTINATION_EDIT = "destination_edit"
    }

    fun back(
        navController: NavHostController,
    ) {
        navController.popBackStack()
    }
}
