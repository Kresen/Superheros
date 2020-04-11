package za.co.superhero.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import za.co.superhero.data.SuperHeroDatabase
import za.co.superhero.model.SuperHero

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val superHeroLiveData = MutableLiveData<SuperHero>()

    fun fetch(uuid: Int) {
        launch {
            val superHero = SuperHeroDatabase(getApplication()).superHeroDao().getSuperHero(uuid)
            superHeroLiveData.value = superHero
        }
    }
}