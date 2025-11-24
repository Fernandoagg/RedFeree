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