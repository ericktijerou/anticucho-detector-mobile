package com.ericktijerou.anticucho_detector.android.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ericktijerou.anticucho_detector.android.ui.camera.CameraScreen
import com.ericktijerou.anticucho_detector.android.ui.result.ResultScreen
import com.ericktijerou.anticucho_detector.android.ui.theme.DetectorTheme

sealed class Screen(val route: String) {
    object Camera : Screen("camera")
    object Result : Screen("result/{names}") {
        const val ARG_NAME_ID: String = "names"
        fun route(names: String) = "result/$names"
    }
}

@Composable
fun NavGraph(startDestination: Screen) {
    val navController = rememberNavController()
    val actions = remember(navController) { Actions(navController) }
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = Modifier.fillMaxSize().background(color = DetectorTheme.colors.background)
    ) {
        composable(Screen.Camera.route) {
            CameraScreen(actions.goToResult)
        }
        composable(Screen.Result.route) {
            val result = it.arguments?.getString(Screen.Result.ARG_NAME_ID).orEmpty()
            ResultScreen(actions.upPress, result)
        }
    }
}

class Actions(navController: NavHostController) {
    val goToResult: (String) -> Unit = {
        navController.navigate(route = it)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
