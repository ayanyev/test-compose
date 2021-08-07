package com.eazzyapps.test.common

import android.os.Parcelable

abstract class Screen {

    abstract val route: String
    abstract val popBackStack: Boolean

    private var routeArgs = listOf<String>()

    var parcelableArgs = mapOf<String, Parcelable>()
        private set

    fun withRouteArgs(vararg args: Any): Screen {
        routeArgs = args.map { "$it" }
        return this
    }

    fun withParcelableArgs(args: Map<String, Parcelable>): Screen {
        parcelableArgs = args
        return this
    }

    val routeWithArgs: String
        get() {
            var result = route
            val placeHolders = Regex("(\\{\\w+\\})").findAll(route).toList()
            val argsIterator = routeArgs.iterator()
            placeHolders.forEach {
                if (argsIterator.hasNext()) {
                    val arg = argsIterator.next()
                    result = result.replaceRange(it.range, "${arg}")
                } else {
                    throw Exception(
                        "Number of arguments (${routeArgs.size}) " +
                                "is less than number of placeholders (${placeHolders.size}) in route"
                    )
                }
            }
            if (argsIterator.hasNext()) {
                throw Exception(
                    "Number of arguments (${routeArgs.size}) " +
                            "is bigger than number of placeholders (${placeHolders.size}) in route"
                )
            }
            return result
        }

}

object Previous : Screen() {
    override val route: String = "back"
    override val popBackStack: Boolean = false
}