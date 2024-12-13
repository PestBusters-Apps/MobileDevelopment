package com.example.pestbusters.retrofit

import com.example.pestbusters.response.PestTreatmentResponse
import com.example.pestbusters.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("/predict")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<PredictResponse>

    @GET("/pest_treatments/getPestTreatments")
    suspend fun getPestTreatments(
        @Query("Pest_name") pestName: String
    ): Response<PestTreatmentResponse>

    @GET("/pest_treatments/getPestTreatments")
    suspend fun getAllPestTreatments(): Response<PestTreatmentResponse>

}
