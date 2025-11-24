package com.example.redferee

import com.google.gson.annotations.SerializedName

// ==========================================
// MODELOS DE HISTORIAL (Rama F3)
// ==========================================
data class PartidoBackend(
    val id: Int,
    val fecha: String,
    val ubicacion: String,
    val deporte: String,
    val nombreArbitro: String?,
    val costo: String?
)

// ==========================================
// MODELOS DE RESEÑAS (Rama F4)
// ==========================================

// 1. Para recibir reseñas
data class Review(
    val NombreArbi: String,
    val Cali: Int,
    val userName: String,
    val Resena: String,
    val tiempo: String
)

// 2. Para recibir la lista de árbitros
data class Referee(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

// 3. Para enviar una nueva reseña
data class ReviewRequest(
    @SerializedName("arbitroid") val arbitroid: Int,
    @SerializedName("usuarioid") val usuarioid: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("texto") val texto: String
)

// 4. Respuesta del servidor al subir reseña
data class PostResponse(
    val status: String,
    val message: String,
    val id_insertado: Int? = null
)

// 5. Contenedor de la respuesta de reseñas
data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)