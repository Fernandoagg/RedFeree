package com.example.redferee

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // --- HISTORIAL ---
    @GET("api/partidos")
    suspend fun obtenerPartidos(): List<PartidoBackend>

    // --- RESEÑAS ---
    @GET("api/resenas/visualizar")
    suspend fun getReviews(@Query("arbitroid") refereeId: String? = null): Response<ApiResponse>

    @GET("api/arbitros/desplegar")
    suspend fun getReferees(): Response<List<Referee>>

    @POST("api/resenas/calificar")
    suspend fun submitReview(@Body request: ReviewRequest): Response<PostResponse>

    // --- CREAR PARTIDO (NUEVO - F2) ---
    // Nota: Ajusta la ruta si tu backend no usa "api/"
    @GET("api/lista-arbitros")
    suspend fun obtenerListaEquipos(): List<EquipoArbitro>

    @POST("api/partidos")
    suspend fun guardarPartido(@Body request: PartidoRequest): PartidoResponse
}

object RetrofitClient {
    // Apunta a tu servidor local
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}