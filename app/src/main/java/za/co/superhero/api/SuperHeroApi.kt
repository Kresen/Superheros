package za.co.superhero.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import za.co.superhero.BuildConfig
import za.co.superhero.model.SuperHero



interface SuperHeroApi {
    @GET("/api/" + BuildConfig.API_DEVELOPER_TOKEN + "/{id}")
    fun getSuperHeroById(@Path("id") id: Int): Single<SuperHero>
}