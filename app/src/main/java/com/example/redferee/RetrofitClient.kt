
package com.example.redferee

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// 1. EL MODELO (Cómo se ven los datos que vienen de Node)
data class PartidoBackend(
    val id: Int,
    val fecha: String,
    val ubicacion: String,
    val deporte: String,
    val nombreArbitro: String?,
    val costo: String?
)

// 2. LA INTERFAZ (Rutas)
interface ApiService {
    @GET("partidos")
    suspend fun obtenerPartidos(): List<PartidoBackend>
}

// 3. LA CONEXIÓN
object RetrofitClient {
    // Si usas EMULADOR, deja esta IP: 10.0.2.2
    // Si usas celular por USB, pon la IP de tu PC (ej: 192.168.1.50)
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}