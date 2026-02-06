package com.example.api_kotlin

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Data class para los usuarios de la API de prueba
data class UserResponse(
    val id: Int,
    val name: String,
    val username: String,
    val email: String
)

// Adaptador para que la respuesta de la API funcione con la UI existente
data class Notice(
    val entity_id: String,
    val forename: String?,
    val name: String?
) {
    val fullName: String
        get() = "${forename ?: ""} ${name ?: ""}".trim()

    // Constructor para convertir un UserResponse en un Notice
    constructor(user: UserResponse) : this(
        entity_id = user.id.toString(),
        forename = user.name.split(" ").firstOrNull(),
        name = user.name.split(" ").drop(1).joinToString(" ")
    )
}

// Las clases InterpolResponse y EmbeddedNotices se han eliminado porque no se usaban.

interface ApiService {
    // El nombre de la función se ha cambiado a getUsers para mayor claridad.
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
