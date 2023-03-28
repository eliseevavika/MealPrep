package com.example.mealprep.fill.out.recipe.card.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.fragment.NavHostFragment
import com.example.mealprep.fill.out.recipe.card.TabScreen
import com.example.mealprep.fill.out.recipe.card.TopBarRecipeCreationForm
import com.example.meaprep.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BlankFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_blank, container, false)

    companion object {
        const val TAG = "ModalBottomSheet"
    }
    }



