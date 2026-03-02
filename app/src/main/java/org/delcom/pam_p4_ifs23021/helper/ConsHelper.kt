package org.delcom.pam_p4_ifs23021.helper

class ConstHelper {
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),

        // ===== Plants (tetap) =====
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),

        // ===== Wisata Samosir =====
        Destinations(path = "destinations"),
        DestinationsAdd(path = "destinations/add"),
        DestinationsDetail(path = "destinations/{destinationId}"),
        DestinationsEdit(path = "destinations/{destinationId}/edit"),
    }
}