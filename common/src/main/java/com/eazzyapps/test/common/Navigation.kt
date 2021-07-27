package com.eazzyapps.test.common

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder


/**
 * Set parcelable arguments to [NavController.getCurrentBackStackEntry]
 * and navigate to a route in the current NavGraph.
 * After navigation get these values back from [NavController.getPreviousBackStackEntry]
 * arguments
 *
 * @param parcelableArgs map of parcelable arguments with respective keys
 * @see navigate(string, NavOptionsBuilder.() -> Unit)
 */
fun NavController.navigate(
    route: String,
    parcelableArgs: Map<String, Parcelable> = mapOf(),
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    currentBackStackEntry?.arguments =
        bundleOf().apply {
            for (arg in parcelableArgs) {
                putParcelable(arg.key, arg.value)
            }
        }
    navigate(route, builder)
}