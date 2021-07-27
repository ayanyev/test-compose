package com.eazzyapps.test.di

import com.eazzyapps.test.common.ActivityDelegate
import org.koin.dsl.module

val appModule = module {

    single {
        // TODO keep an eye on if delegate singleton works fine
        //  if there are more then one Activity in app
        ActivityDelegate()
    }

}