package com.example.pestbusters.response

data class PredictResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val bounding_box: List<Double>,
    val class_label: String,
    val confidence: Double
)
