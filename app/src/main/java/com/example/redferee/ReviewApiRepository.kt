package com.example.redferee

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Asegúrate de que esta IP sea correcta para tu emulador (10.0.2.2)
private const val BASE_URL = "http://10.0.2.2:3000/"

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

class ReviewApiRepository {
    private val apiService = RetrofitClient.apiService

    // FUNCIÓN 1: Obtener todas las reseñas (GET)
    suspend fun fetchAllReviews(): Result<List<Review>> {
        return try {
            val response = apiService.getReviews()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.reviews)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("API", "Error fetching reviews: $errorMsg")
                Result.failure(Exception("Error API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("API", "Exception fetching reviews", e)
            Result.failure(e)
        }
    }

    // FUNCIÓN 2: Obtener lista de árbitros (GET)
    suspend fun fetchReferees(): Result<List<Referee>> {
        return try {
            val response = apiService.getReferees()

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("API", "Error fetching referees: $errorMsg")
                Result.failure(Exception("Error al cargar árbitros"))
            }
        } catch (e: Exception) {
            Log.e("API", "Exception fetching referees", e)
            Result.failure(e)
        }
    }

    // FUNCIÓN 3: Subir una reseña (POST)
    suspend fun submitReview(request: ReviewRequest): Result<String> {
        return try {
            val response = apiService.submitReview(request)

            if (response.isSuccessful) {
                Result.success("Reseña subida con éxito")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("API", "Error submitting review: $errorMsg")
                Result.failure(Exception("Error al subir: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("API", "Exception submitting review", e)
            Result.failure(e)
        }
    }
}
class UserApiRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.registerUser(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
class UserApiRepositoryLogin {
    private val apiService = RetrofitClient.apiService

    suspend fun registerUser(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.registerUser(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = apiService.loginUser(request)
            if (response.isSuccessful && response.body() != null) {
                // Si tu API devuelve exactamente el objeto del usuario
                Result.success(response.body()!!)
            } else {
                // Si la API responde con error 4xx/5xx
                val msg = "Error: ${response.code()} ${response.message()}"
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
