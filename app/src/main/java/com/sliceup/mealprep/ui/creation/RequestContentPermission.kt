package com.sliceup.mealprep.ui.creation

import android.net.Uri
import android.os.Build
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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.sliceup.mealprep.ui.theme.MealPrepColor
import com.sliceup.mealprep.viewmodel.RecipeViewModel
import com.sliceup.mealprep.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestContentPermission(viewModel: () -> RecipeViewModel) {
    val imageUri by viewModel().uri.collectAsState()

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
                    viewModel().setPhoto("")
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
            viewModel().setPhoto(it.toString())
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

