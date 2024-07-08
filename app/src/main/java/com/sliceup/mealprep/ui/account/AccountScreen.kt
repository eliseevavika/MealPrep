package com.sliceup.mealprep.ui.account

import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.sliceup.mealprep.ui.navigation.BottomNavigationBar
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB1
import com.sliceup.mealprep.ui.theme.fontFamilyForBodyB2
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.R
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AccountScreen(
    navController: () -> NavHostController, viewModal: () -> RecipeViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { selectedFileUri ->
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(selectedFileUri)
                viewModal().importDataFromFile(inputStream, showSuccessMessage = {
                    android.app.AlertDialog.Builder(context)
                        .setMessage("Your file has been uploaded successfully.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }, showError = {
                    android.app.AlertDialog.Builder(context).setMessage(it)
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                })
            }
        }

    Scaffold(topBar = {
        TopBarForSettingsScreen()
    }, bottomBar = {
        BottomNavigationBar(navController = navController)
    }, content = { padding ->
        Column(
            modifier = Modifier.padding(
                top = 30.dp, start = 16.dp, end = 16.dp, bottom = 60.dp
            ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1F)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.noimage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = MealPrepColor.grey_600,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(2F)
                ) {
                    Text(
                        text = "Profile", fontFamily = fontFamilyForBodyB1,
                        fontSize = 20.sp, textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                    Text(
                        text = viewModal().getUserEmail() ?: "", fontFamily = fontFamilyForBodyB2,
                        fontSize = 16.sp, textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 18.dp))

            Divider(
                color = MealPrepColor.grey_600,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedButton(border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                onClick = {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "application/json"
                    val mimeType = "application/json"
                    filePickerLauncher.launch(mimeType)
                },
                content = {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                color = MaterialTheme.colors.onSurface,
                                text = "Import data from a json file"
                            )
                        })
                })

            Spacer(modifier = Modifier.padding(vertical = 18.dp))

            OutlinedButton(border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
                onClick = {
                    scope.launch {
                        val json = viewModal().export()

                        if (json != null) {
                            val timestamp = System.currentTimeMillis()
                            val file = File(context.filesDir, "sliceUpNew_$timestamp.json")
                            json.let { file.writeText(it) }

                            val uri = FileProvider.getUriForFile(
                                context,
                                context.applicationContext.packageName + ".fileprovider",
                                file
                            )
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_STREAM, uri)
                            }
                            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            context.startActivity(sendIntent)
                        }else{
                            Log.e("ExportError", "Exported JSON is null")
                        }
                    }
                },
                content = {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Text(
                                color = MaterialTheme.colors.onSurface,
                                text = "Export all your data"
                            )
                        })
                })
            Divider(
                color = MealPrepColor.grey_600,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.padding(vertical = 18.dp))
        }
    })
}