package com.example.mealprep.fill.out.recipe.card

import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider.getUriForFile
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.mealprep.viewmodel.RecipeViewModel
import com.example.meaprep.R
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestContentPermission(viewModel: () -> RecipeViewModel) {
    val imageUri by viewModel().uri.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel().setImageUri(uri)
    }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .size(coil.size.Size.ORIGINAL)
            .build()
    )

    Box {
        if (imageUri != null) {
            ConstraintLayout {
                val (photo, remove, edit) = createRefs()

                if (painter.state is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator()
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(top = 16.dp, bottom = 16.dp)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .constrainAs(photo) {
                                top.linkTo(parent.top, margin = 16.dp)
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                IconButton(modifier = Modifier
                    .constrainAs(remove) {
                        bottom.linkTo(photo.bottom, margin = 16.dp)
                    }
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = MealPrepColor.white,
                            radius = this.size.maxDimension / 2.0f
                        )
                    }, onClick = {
                    viewModel().setImageUri(null)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_delete_24),
                        contentDescription = "Delete photo",
                        tint = MealPrepColor.orange
                    )
                }
                IconButton(modifier = Modifier
                    .constrainAs(edit) {
                        end.linkTo(photo.end)
                        bottom.linkTo(photo.bottom, margin = 16.dp)
                    }
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = MealPrepColor.white,
                            radius = this.size.maxDimension / 2.0f
                        )
                    }, onClick = {
                    launcher.launch("image/*")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_edit_24),
                        contentDescription = "Edit photo",
                        tint = MealPrepColor.orange
                    )
                }
            }
        } else {
            ShowNoImage(launcher)
        }

        imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val imageData = inputStream?.readBytes()
            inputStream?.close()

            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile("image_", ".jpg", storageDir)

            val outputStream = FileOutputStream(imageFile)
            outputStream.write(imageData)
            outputStream.close()

            val contentUri: Uri = getUriForFile(context, "com.example.meaprep.fileprovider", imageFile)
            viewModel().setPhoto(contentUri.toString())
        }
    }
}

@Composable
private fun ShowNoImage(launcher: ManagedActivityResultLauncher<String, Uri?>) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 16.dp)
        .height(220.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MealPrepColor.grey_100),
        onClick = {
            launcher.launch("image/*")
        }) {
        Column {
            Icon(
                modifier = Modifier.align(CenterHorizontally),
                painter = painterResource(id = R.drawable.outline_photo_camera_24),
                contentDescription = "Add photo",
                tint = MealPrepColor.orange
            )
            Text(text = "Add photo", color = MealPrepColor.orange)
        }
    }
}

