package com.tirexmurina.shiftlabtest2024sp.utils

import androidx.fragment.app.Fragment
import com.tirexmurina.shiftlabtest2024sp.presentation.MainActivity

val Fragment.mainActivity: MainActivity
    get() = requireActivity() as MainActivity