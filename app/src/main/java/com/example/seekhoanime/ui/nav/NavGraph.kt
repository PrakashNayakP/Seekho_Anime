package com.example.seekhoanime.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seekhoanime.ui.detail.DetailScreen
import com.example.seekhoanime.ui.detail.DetailViewModel
import com.example.seekhoanime.ui.home.HomeScreen
import com.example.seekhoanime.ui.home.HomeViewModel

@Composable
fun AppNavGraph(modifier: Modifier = Modifier, startDestination: String = "home") {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination, modifier = modifier) {
        composable("home") {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(viewModel = vm, onAnimeClick = { id ->
                navController.navigate("detail/$id")
            })
        }

        composable("detail/{id}") { backStackEntry ->
            val vm: DetailViewModel = hiltViewModel(backStackEntry)
            DetailScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
    }
}
