package za.co.superhero.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken


class ListStringConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListLisr(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}

@TypeConverters(ListStringConverter::class)
data class Appearance(
    val eyeColor: String?,
    val gender: String?,
    val hairColor: String?,
    val height: List<String>?,
    val race: String?,
    val weight: List<String>?
)

@TypeConverters(ListStringConverter::class)
data class Biography(
    val aliases: List<String>?,
    val alignment: String?,
    @SerializedName("alter-egos")
    val alterEgos: String?,
    @SerializedName("first-appearance")
    val firstAppearance: String?,
    @SerializedName("full-name")
    val fullName: String?,
    @SerializedName("place-of-birth")
    val placeOfBirth: String?,
    val publisher: String?
)

data class Connections(
    @SerializedName("group-affiliation")
    val groupAffiliation: String?,
    val relatives: String?
)


data class Image(
    val url: String?
)


data class Powerstats(
    val combat: Int?,
    val durability: Int?,
    val intelligence: Int?,
    val power: Int?,
    val speed: Int?,
    val strength: Int?
)

data class Work(
    val base: String?,
    val occupation: String?
)

@Entity(tableName = "superhero")
data class SuperHero(
    @Embedded
    val appearance: Appearance?,
    @Embedded
    val biography: Biography?,
    @Embedded
    val connections: Connections?,
    @PrimaryKey
    val id: Int,
    @Embedded
    val image: Image?,
    val name: String = "",
    @Embedded
    val powerstats: Powerstats?,
    val slug: String?,
    @Embedded
    val work: Work?
)

data class SuperHeroPalette(var color: Int = 0)

