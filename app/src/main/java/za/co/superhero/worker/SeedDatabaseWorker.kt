package za.co.superhero.worker

import android.content.Context
import androidx.work.CoroutineWorker

import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import za.co.superhero.data.SuperHeroDatabase
import za.co.superhero.model.SuperHero

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {

            try {
                applicationContext.assets.open("set.json").use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val type = object : TypeToken<List<SuperHero>>() {}.type
                        val list: List<SuperHero> = Gson().fromJson(jsonReader, type)

                        SuperHeroDatabase(applicationContext).superHeroDao().insertAll(list)

                        Result.success()
                    }
                }
            } catch (e: Exception) {
                //Timber.e(e, "Error seeding database")
                Result.failure()
            }
        }
    }
}