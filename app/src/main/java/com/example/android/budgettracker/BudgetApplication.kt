package com.example.android.budgettracker

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.jakewharton.threetenabp.AndroidThreeTen

class BudgetApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    println("testing")
    AndroidThreeTen.init(this)
    if (BuildConfig.DEBUG) {// && FlipperUtils.shouldEnableFlipper(this)) {
      val client = AndroidFlipperClient.getInstance(this)
      client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
      val descriptorMapping = DescriptorMapping.withDefaults()
      client.addPlugin(InspectorFlipperPlugin(this, descriptorMapping))
      client.addPlugin(DatabasesFlipperPlugin(this))
      client.start()
    }
  }
}