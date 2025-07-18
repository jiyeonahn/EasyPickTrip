import com.jiyeon.easypicktrip.response.geocode.GeocodeResponse
import com.jiyeon.easypicktrip.response.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") query: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): GeocodeResponse

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr"
    ): WeatherResponse
}