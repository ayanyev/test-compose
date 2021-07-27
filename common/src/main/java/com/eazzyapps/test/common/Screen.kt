package com.eazzyapps.test.common

import android.os.Parcelable

interface Screen {

    val route: String
    val popBackStack: Boolean
    val parcelableArgs: Map<String, Parcelable>

}

object Previous : Screen {
    override val route: String = "back"
    override val popBackStack: Boolean = false
    override val parcelableArgs: Map<String, Parcelable> = mapOf()
}