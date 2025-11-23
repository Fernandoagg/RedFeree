package com.example.redferee

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// --- MODELOS DE DATOS ---

data class Review(
    val NombreArbi: String,
    val Cali: Int,
    val userName: String,
    val Resena: String,
    val tiempo: String
)

data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)

// --- INTERFAZ DE LA API ---

interface ApiService {

    @GET("api/resenas/visualizar")
    suspend fun getReviews(
        @Query("arbitroid") refereeId: String? = null
    ): Response<ApiResponse>
}