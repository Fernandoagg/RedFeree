package com.example.redferee

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// --- MODELOS DE DATOS ---
// LoginRequest y LoginResponse
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

// Según el comportamiento de tu backend (igual que el registro)
data class LoginResponse(
    val nombre: String,
    val email: String,
    val password: String,
    val tipo: String
)

// Registro de usuario
data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("tipo") val tipo: String = "usuario"
)

// IMPORTANTE:
// Si tu API responde con "id_insertado", se debe mapear así:
data class RegisterResponse(
    val status: String,
    val message: String,
    @SerializedName("id_insertado") val userId: Int? = null
)
// SI tu backend devuelve "userId" directamente, usa esta en su lugar:
// data class RegisterResponse(
//    val status: String,
//    val message: String,
//    val userId: Int? = null
// )

// Reseñas
data class Review(
    val NombreArbi: String,
    val Cali: Int,
    val userName: String,
    val Resena: String,
    val tiempo: String
)

// Árbitros
data class Referee(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// Enviar reseña
data class ReviewRequest(
    @SerializedName("arbitroid") val arbitroid: Int,
    @SerializedName("usuarioid") val usuarioid: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("texto") val texto: String
)

// Respuesta al enviar reseña
data class PostResponse(
    val status: String,
    val message: String,
    @SerializedName("id_insertado") val idInsertado: Int? = null
)

// Contenedor reseñas
data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)

// --- INTERFAZ API ---
interface ApiService {

    @GET("api/resenas/visualizar")
    suspend fun getReviews(
        @Query("arbitroid") refereeId: String? = null
    ): Response<ApiResponse>

    @GET("api/arbitros/desplegar")
    suspend fun getReferees(): Response<List<Referee>>

    @POST("api/resenas/calificar")
    suspend fun submitReview(@Body request: ReviewRequest): Response<PostResponse>

    @POST("api/usuarios/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>
    @POST("api/usuarios/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}
