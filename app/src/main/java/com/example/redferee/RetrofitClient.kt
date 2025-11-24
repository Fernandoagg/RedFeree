package com.example.redferee

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// --- 1. INTERFAZ CON TODAS LAS RUTAS (Endpoints) ---
interface ApiService {

    // --- RUTAS DE HISTORIAL ---
    @GET("partidos")
    suspend fun obtenerPartidos(): List<PartidoBackend>

    // --- RUTAS DE RESEÑAS ---
    // Nota: Ya no ponemos "api/" al principio porque la BASE_URL ya lo tiene.
    
    @GET("resenas/visualizar")
    suspend fun getReviews(
        @Query("arbitroid") refereeId: String? = null
    ): Response<ApiResponse>

    @GET("arbitros/desplegar")
    suspend fun getReferees(): Response<List<Referee>>

    @POST("resenas/calificar")
    suspend fun submitReview(@Body request: ReviewRequest): Response<PostResponse>
}

// --- 2. OBJETO DE CONEXIÓN ÚNICO ---
object RetrofitClient {
    // Apunta a tu servidor local
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}