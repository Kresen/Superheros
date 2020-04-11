package za.co.superhero.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import za.co.superhero.api.SuperHeroApiService
import za.co.superhero.data.SuperHeroDatabase
import za.co.superhero.model.SuperHero
import za.co.superhero.util.SharedPreferencesHelper
import java.lang.NumberFormatException
import kotlin.random.Random

public class ListViewModel(application: Application): BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val superHeroService = SuperHeroApiService()
    private val disposable = CompositeDisposable()

    val superHeros = MutableLiveData<List<SuperHero>>()
    val superHeroLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
     //   checkCacheDuration()
//        val updateTime = prefHelper.getUpdateTime()
//        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
//            fetchFromDatabase()
//        } else {
//            fetchFromRemote()
//        }

        fetchFromDatabase()
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = SuperHeroDatabase(getApplication()).superHeroDao().getAllSuperHeros()
            superHeroRetrieved(dogs)
            Toast.makeText(getApplication(), "Superheros retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

     fun ClearDatabase() {
        loading.value = true
        launch {
            val dogs = SuperHeroDatabase(getApplication()).superHeroDao().deleteAllSuperHeros()
            superHeroRetrieved(null)
            Toast.makeText(getApplication(), "All Superheros Removed from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true

        disposable.add(
            superHeroService.getSuperHeroById(Random.nextInt(731))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<SuperHero>() {

                    override fun onSuccess(dogList: SuperHero) {
                        storeHeroLocally(dogList)

                        Toast.makeText(getApplication(), "A new hero has been retrieved from endpoint", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        superHeroLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }


                })
        )
    }

    private fun superHeroRetrieved(dogList: List<SuperHero>?) {

        Log.i("dogs", dogList.toString())
        superHeros.value = dogList
        superHeroLoadError.value = false
        loading.value = false
    }

    private fun storeHeroLocally(list: SuperHero) {
        launch {
            val dao = SuperHeroDatabase(getApplication()).superHeroDao()
          //  dao.deleteAllSuperHeros()
            val result = dao.insert(list)
            var i = 0
         //   while (i < list.size) {
          //      list[i].uuid = result[i].toInt()
          //      ++i
         //   }
            var suplist = listOf<SuperHero>(list)

            superHeroRetrieved(suplist)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}