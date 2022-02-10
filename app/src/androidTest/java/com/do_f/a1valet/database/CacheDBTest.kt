package com.do_f.a1valet.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.do_f.a1valet.database.dao.DeviceDao
import com.do_f.a1valet.database.entity.Device
import com.do_f.a1valet.extensions.getOrAwaitValue
import junit.framework.TestCase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CacheDBTest : TestCase() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: CacheDB
    private lateinit var dao: DeviceDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CacheDB::class.java).build()
        dao = db.deviceDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeAndReadDevice() {
        val device = Device(
            id = 1337,
            title = "iPhone 13 Pro",
            coverUrl = "https://www.google.fr/iPhone.png",
            currency = "CAD",
            description = "Lorem Ipsum",
            imageUrl = "https://www.google.fr/iPhone.png",
            isFavorite = true,
            price = 1337,
            type = "iphone"
        )

        dao.insertDevice(device)
        val items = dao.getDevices().getOrAwaitValue()
        assertTrue(items.contains(device))
    }

    @Test
    fun testSearch() {
        val items = listOf(
            Device(
                id = 1337,
                title = "iPhone 13 Pro",
                coverUrl = "https://www.google.fr/iPhone.png",
                currency = "CAD",
                description = "Lorem Ipsum",
                imageUrl = "https://www.google.fr/iPhone.png",
                isFavorite = true,
                price = 1337,
                type = "iphone"
            ),
            Device(
                id = 1338,
                title = "Pixel 6 Pro",
                coverUrl = "https://www.google.fr/iPhone.png",
                currency = "CAD",
                description = "Lorem Ipsum",
                imageUrl = "https://www.google.fr/iPhone.png",
                isFavorite = true,
                price = 1337,
                type = "android"
            )
        )

        dao.insertDevices(items)
        val results = dao.search("iphone").getOrAwaitValue()
        assertTrue(results.size == 1)

        val resultsEmpty = dao.search("smartlock").getOrAwaitValue()
        assertTrue(resultsEmpty.isEmpty())
    }

    @Test
    fun testFilter() {
        val items = listOf(
            Device(
                id = 1337,
                title = "iPhone 13 Pro",
                coverUrl = "https://www.google.fr/iPhone.png",
                currency = "CAD",
                description = "Lorem Ipsum",
                imageUrl = "https://www.google.fr/iPhone.png",
                isFavorite = false,
                price = 1337,
                type = "iphone"
            ),
            Device(
                id = 1338,
                title = "Pixel 6 Pro",
                coverUrl = "https://www.google.fr/iPhone.png",
                currency = "CAD",
                description = "Lorem Ipsum",
                imageUrl = "https://www.google.fr/iPhone.png",
                isFavorite = false,
                price = 1337,
                type = "android"
            )
        )

        dao.insertDevices(items)

        val resultsIsFavorite = dao.filterByFavorite(true).getOrAwaitValue()
        assert(resultsIsFavorite.isEmpty())

        val resultsIsNoneFavorite = dao.filterByFavorite(false).getOrAwaitValue()
        assert(resultsIsNoneFavorite.size == 2)

        val resultsFilterByType = dao.filterByType("android").getOrAwaitValue()
        assert(resultsFilterByType.size == 1)
    }
}