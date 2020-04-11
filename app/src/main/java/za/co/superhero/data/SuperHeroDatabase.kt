package za.co.superhero.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import za.co.superhero.model.SuperHero
import za.co.superhero.worker.SeedDatabaseWorker

@Database(entities = arrayOf(SuperHero::class), version = 1)
abstract class SuperHeroDatabase: RoomDatabase() {
    abstract fun superHeroDao(): SuperHeroDao

    companion object {
        @Volatile private var instance: SuperHeroDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            SuperHeroDatabase::class.java,
            "superherodatabase"
        )            .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                WorkManager.getInstance(context).enqueue(request)
            }
        }).build()
    }
}