package com.example.mealprep.fill.out.recipe.card

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mealprep.Converters
import com.example.mealprep.fill.out.recipe.card.creation.RecipeCreationViewModel
import com.example.mealprep.ui.theme.MealPrepColor
import com.example.meaprep.R
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestContentPermission(viewModel: RecipeCreationViewModel) {
    val photoStrState = viewModel.photo.collectAsState()

    val imageUri = viewModel.uri.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setImageUri(uri)
    }
    Box {
        if (imageUri.value != null) {
            ConstraintLayout {
                val (photo, remove, edit) = createRefs()
                var bitmap = Converters().converterStringToBitmap(photoStrState.value)

                bitmap?.let { btm ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
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
                    IconButton(modifier = Modifier
                        .constrainAs(remove) {
                            bottom.linkTo(photo.bottom, margin = 16.dp)
                        }
                        .padding(16.dp)
                        .drawBehind {
                            drawCircle(
                                color = MealPrepColor.white, radius = this.size.maxDimension / 2.0f
                            )
                        }, onClick = {
                        viewModel.setImageUri(null)
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
                                color = MealPrepColor.white, radius = this.size.maxDimension / 2.0f
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
            }
        } else {
            ShowNoImage(launcher)
        }

        imageUri.value?.let {
            if (Build.VERSION.SDK_INT < 28) {
                val bitmapValue = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                viewModel.setPhoto(Converters().convertBitmapToString(bitmapValue!!))

            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                val bitmapValue = ImageDecoder.decodeBitmap(source)
                viewModel.setPhoto(Converters().convertBitmapToString(bitmapValue!!))
            }
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
        Column() {
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