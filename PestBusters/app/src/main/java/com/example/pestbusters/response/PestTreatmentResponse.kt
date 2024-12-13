package com.example.pestbusters.response

data class PestTreatmentResponse(
    val data: List<PestData>
)

data class PestData(
    val Created_at: String,
    val Image: String,
    val PestId: Int,
    val Pest_name: String,
    val Treat_material: String,
    val Treatment: String
)
