package com.example.redferee

class ReviewApiRepository {

    // CORRECCIÓN AQUÍ:
    // Antes devolvía Result<ApiResponse> (La caja cerrada)
    // Ahora devuelve Result<List<Review>> (Los dulces sueltos)
    suspend fun fetchAllReviews(refereeId: String? = null): Result<List<Review>> {
        return try {
            val response = RetrofitClient.apiService.getReviews(refereeId)

            if (response.isSuccessful && response.body() != null) {
                // AQUÍ ESTÁ EL TRUCO:
                // Tomamos el cuerpo (.body) y sacamos solo la lista (.reviews)
                Result.success(response.body()!!.reviews)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Estas dos funciones de abajo ESTABAN BIEN, las dejamos igual.
    suspend fun fetchReferees(): Result<List<Referee>> {
        return try {
            val response = RetrofitClient.apiService.getReferees()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar árbitros"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitReview(request: ReviewRequest): Result<PostResponse> {
        return try {
            val response = RetrofitClient.apiService.submitReview(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al enviar"))
            }
        } catch (e: Exception) {
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
