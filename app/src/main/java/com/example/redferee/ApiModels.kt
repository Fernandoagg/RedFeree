package com.example.redferee

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// --- MODELOS DE DATOS ---

// 1. Para recibir reseñas (GET /api/resenas/visualizar)
data class Review(
    val NombreArbi: String,
    val Cali: Int,
    val userName: String,
    val Resena: String,
    val tiempo: String
)

// 2. Para recibir la lista de árbitros (GET /api/arbitros)
data class Referee(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// 3. Para enviar una nueva reseña (POST /api/resenas)
// Los nombres deben coincidir con lo que espera tu body en Node.js
data class ReviewRequest(
    @SerializedName("arbitroid") val arbitroid: Int,
    @SerializedName("usuarioid") val usuarioid: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("texto") val texto: String
)

// 4. Para leer la respuesta del servidor al hacer POST
data class PostResponse(
    val status: String,
    val message: String,
    val id_insertado: Int? = null
)

// Contenedor de la respuesta de reseñas
data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)

// --- INTERFAZ DE LA API (Endpoints) ---

interface ApiService {

    // Obtener la lista de reseñas (para la pantalla principal)
    @GET("api/resenas/visualizar")
    suspend fun getReviews(
        @Query("arbitroid") refereeId: String? = null
    ): Response<ApiResponse>

    // Obtener la lista de árbitros (para el spinner del formulario)
    @GET("api/arbitros/desplegar")
    suspend fun getReferees(): Response<List<Referee>>

    // Subir una nueva reseña
    @POST("api/resenas/calificar")
    suspend fun submitReview(@Body request: ReviewRequest): Response<PostResponse>
}