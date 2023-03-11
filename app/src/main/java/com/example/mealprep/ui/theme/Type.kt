package com.example.mealprep.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.meaprep.R

// Set of Material typography styles to start with
@OptIn(ExperimentalTextApi::class)
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
val fontNameForBodyB1 = GoogleFont("Abhaya Libre")
@OptIn(ExperimentalTextApi::class)
val fontFamilyForBodyB1 = FontFamily(
    Font(
        googleFont = fontNameForBodyB1,
        fontProvider = provider,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal
    )
)

@OptIn(ExperimentalTextApi::class)
val fontNameForBodyB2 = GoogleFont("Abyssinica SIL")
@OptIn(ExperimentalTextApi::class)
val fontFamilyForBodyB2 = FontFamily(
    Font(
        googleFont = fontNameForBodyB2,
        fontProvider = provider,
        weight = FontWeight.W400,
        style = FontStyle.Normal
    )
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = fontFamilyForBodyB1,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,

    ),
    body2 = TextStyle(
        fontFamily = fontFamilyForBodyB2,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,

        )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */

)