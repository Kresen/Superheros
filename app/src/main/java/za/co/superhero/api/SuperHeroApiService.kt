package za.co.superhero.api

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import za.co.superhero.model.SuperHero

class SuperHeroApiService {

    private val BASE_URL = "https://superheroapi.com"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(SuperHeroApi::class.java)

    fun getSuperHeroById(id:Int): Single<SuperHero> {
        return api.getSuperHeroById(id)
    }
}