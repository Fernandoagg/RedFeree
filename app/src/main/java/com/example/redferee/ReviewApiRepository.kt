package com.example.redferee

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * URL Base de tu API de Express.
 */
private const val BASE_URL = "http://10.0.2.2:3000/"

// Singleton para inicializar Retrofit
object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

/**
 * Repositorio que maneja la lógica de la llamada API (GET /api/resenas).
 */
class ReviewApiRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun fetchAllReviews(): Result<List<Review>> {
        return try {
            val response = apiService.getReviews()

            if (response.isSuccessful && response.body() != null && response.body()!!.status == "ok") {
                // Éxito: devuelve la lista de reseñas
                val apiResponse = response.body()!!
                Log.d("API", "Reseñas cargadas: ${apiResponse.count}")
                Result.success(apiResponse.reviews)
            } else {
                // Fallo en la respuesta de la API o JSON no esperado
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("API", "Fallo al obtener reseñas: ${response.code()} - $errorBody")
                Result.failure(Exception("Fallo en la API: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            // Error de conexión (Ej: servidor Express apagado o URL incorrecta)
            Log.e("API", "Error de conexión/parsing. URL: ${BASE_URL}", e)
            Result.failure(e)
        }
    }
}