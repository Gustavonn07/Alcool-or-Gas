package com.example.alcoolorgas.models

data class FuelStation(
    val id: String,
    val name: String,
    val alcool: Double,
    val gasolina: Double,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val date: Long = System.currentTimeMillis(),
    val percent: Int
)
