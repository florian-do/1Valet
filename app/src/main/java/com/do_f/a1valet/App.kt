package com.do_f.a1valet

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.do_f.a1valet.database.CacheDB
import com.do_f.a1valet.database.entity.Device
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader

class App: Application() {
    companion object {
        lateinit var cacheDb: CacheDB
    }

    override fun onCreate() {
        super.onCreate()
        cacheDb = Room.databaseBuilder(this, CacheDB::class.java, CacheDB.name)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val inputStream = resources.openRawResource(R.raw.devices_data)
                    val content = inputStream.bufferedReader().use(BufferedReader::readText)
                    val listUserType = object : TypeToken<List<Device>>() {}.type
                    val objects: List<Device> = Gson().fromJson(content, listUserType)
                    GlobalScope.launch {
                        cacheDb.deviceDao().insertDevices(objects)
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}