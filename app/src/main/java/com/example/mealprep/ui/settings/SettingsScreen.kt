package com.example.mealprep.fill.out.recipe.card.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.example.mealprep.fill.out.recipe.card.TopBarForSettingsScreen
import com.example.mealprep.ui.navigation.BottomNavigationBar
import com.example.mealprep.viewmodel.RecipeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SettingsScreen(
    navController: () -> NavHostController,
    viewModal: () -> RecipeViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { selectedFileUri ->
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(selectedFileUri)
                viewModal().importDataFromFile(inputStream, showError = {
                    android.app.AlertDialog.Builder(context)
                        .setMessage(it)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                })
            }
        }

    Scaffold(topBar = {
        TopBarForSettingsScreen()
    },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier.padding(
                    top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
                ), verticalArrangement = Arrangement.Top
            ) {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "application/json" // Specify the MIME type for JSON files
                    val mimeType = "application/json"
                    filePickerLauncher.launch(mimeType)

                }) {
                    Text(text = "Import")
                }
                Button(onClick = {
                    scope.launch {
                        val json = viewModal().export()

                        val file = File(context.filesDir, "sliceUpNew.json")
                        json?.let { file.writeText(it) }

                        val uri = FileProvider.getUriForFile(
                            context,
                            context.applicationContext.packageName + ".provider",
                            file
                        )
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_STREAM, uri)
                        }
                        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        context.startActivity(sendIntent)
                    }
                }
                ) {
                    Text(text = "Export")
                }
                IconButton(onClick = { Firebase.auth.signOut() }) {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = null,
                    )
                }
            }
        })
}

