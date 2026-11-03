package com.kidsenglish.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Root [Application] class for Kids English initializing Hilt dependency injection.
 */
@HiltAndroidApp
class KidsEnglishApplication : Application()
