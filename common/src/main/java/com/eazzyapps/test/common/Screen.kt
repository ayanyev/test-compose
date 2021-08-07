package com.eazzyapps.test.common

import android.os.Parcelable

abstract class Screen {

    abstract val route: String
    abstract val popBackStack: Boolean

    private var routeArgs = listOf<Any>()

    var parcelableArgs = mapOf<String, Parcelable>()
        private set

    fun withRouteArgs(vararg args: Any): Screen {
        routeArgs = listOf(args)
        return this
    }

    fun withParcelableArgs(args: Map<String, Parcelable>): Screen {
        parcelableArgs = args
        return this
    }

    val routeWithArgs: String
        get() {
            var result = route
            val regex = Regex.fromLiteral("({\\w+})")
            val placeHolders = regex.findAll(route).toList()
            val argsIterator = routeArgs.iterator()
            placeHolders.forEach {
                if (argsIterator.hasNext()) {
                    result = result.replaceRange(it.range, "${argsIterator.next()}")
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