package com.example.mycmapplication.navigation

sealed class Screens(val route: String) {
    object MainRoll : Screens("main_roll")
    object RickAndMorty : Screens("rick_and_morty")
    object DieResult1 : Screens("die_result_1/{temp}") {
        fun createRoute(temp: Int) = "die_result_1/$temp"
    }
    object DieResult2 : Screens("die_result_2/{dieValue}") {
        fun createRoute(value: Int) = "die_result_2/$value"
    }
    object DieResult3 : Screens("die_result_3/{dieValue}") {
        fun createRoute(value: Int) = "die_result_3/$value"
    }
    object DieResult4 : Screens("die_result_4")
    object DieResult5 : Screens("die_result_5/{dieValue}") {
        fun createRoute(value: Int) = "die_result_5/$value"
    }
    object DieResult6 : Screens("die_result_6/{dieValue}") {
        fun createRoute(value: Int) = "die_result_6/$value"
    }
}
