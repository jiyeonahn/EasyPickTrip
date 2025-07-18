package com.jiyeon.easypicktrip.repository

import android.util.Log
import com.jiyeon.easypicktrip.MainActivity
import com.jiyeon.easypicktrip.client.WeatherApiClient

class WeatherRepository {
    private val apiService = WeatherApiClient.apiService
    
    suspend fun getWeatherData(cityName: String, countryCode: String, apiKey: String): MainActivity.WeatherData? {
        return try {
            // 1. 좌표 가져오기
            val coordinates = apiService.getCoordinates("$cityName,$countryCode", 1, apiKey)
            if (coordinates.isEmpty()) {
                Log.w("WeatherRepository", "No coordinates found for $cityName, $countryCode")
                return null
            }
            Log.d("result--> ",coordinates[0].toString())
            val location = coordinates[0]
            Log.d("WeatherRepository", "Found coordinates: ${location.lat}, ${location.lon}")
            
            // 2. 날씨 정보 가져오기
            val weatherResponse = apiService.getCurrentWeather(
                location.lat, 
                location.lon, 
                apiKey
            )

            MainActivity.WeatherData(
                temperature = weatherResponse.main.temp,
                description = weatherResponse.weather[0].description,
                humidity = weatherResponse.main.humidity,
                windSpeed = weatherResponse.wind?.speed ?: 0.0,
                iconCode = weatherResponse.weather[0].icon
            )
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching weather data", e)
            null
        }
    }
}