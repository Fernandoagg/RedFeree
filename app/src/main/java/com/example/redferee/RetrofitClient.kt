package com.example.redferee

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// --- IMPORTS INTERNOS FALTANTES (¡AGREGADOS!) ---
// La compilación fallaba porque no podía ver estas clases
import com.example.redferee.PartidoBackend
import com.example.redferee.ApiResponse
import com.example.redferee.Referee
import com.example.redferee.ReviewRequest
import com.example.redferee.PostResponse
import com.example.redferee.EquipoArbitro
import com.example.redferee.PartidoRequest
import com.example.redferee.PartidoResponse
import com.example.redferee.RegisterRequest
import com.example.redferee.RegisterResponse
import com.example.redferee.LoginRequest
import com.example.redferee.LoginResponse


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

    // --- CREAR PARTIDO ---
    @GET("api/lista-arbitros")
    suspend fun obtenerListaEquipos(): List<EquipoArbitro>

    @POST("api/partidos")
    suspend fun guardarPartido(@Body request: PartidoRequest): PartidoResponse

    // --- AUTENTICACIÓN (LOGIN/REGISTER) ---
    @POST("api/usuarios/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/usuarios/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
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