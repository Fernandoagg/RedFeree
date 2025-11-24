package com.example.redferee

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// ----------------------------------------------------------
// 1. EL MODELO DE DATOS (JSON)
// Así esperamos que Node.js nos mande la información.
// ----------------------------------------------------------
data class PartidoBackend(
    val id: Int,
    val fecha: String,
    val ubicacion: String,
    val deporte: String,
    // Ponemos '?' porque si tu tabla SQL no tiene estos campos o el JOIN falla,
    // la app no tronará, simplemente vendrá nulo.
    val nombreArbitro: String?,
    val costo: String?
)

// ----------------------------------------------------------
// 2. LA INTERFAZ (RUTAS)
// Aquí listamos las URLs de tu backend
// ----------------------------------------------------------
interface ApiService {
    // Esto busca en: http://10.0.2.2:3000/api/partidos
    // Si tu ruta en Node es solo "/partidos", avísame para quitarle el "api/" abajo
    @GET("partidos")
    suspend fun obtenerPartidos(): List<PartidoBackend>
}

// ----------------------------------------------------------
// 3. EL CLIENTE RETROFIT (CONFIGURACIÓN)
// ----------------------------------------------------------
object RetrofitClient {
    // IMPORTANTE:
    // Si usas EMULADOR: usa "http://10.0.2.2:3000/api/"
    // Si usas CELULAR FÍSICO: usa la IP de tu PC (ej: "http://192.168.1.50:3000/api/")
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}