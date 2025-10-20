package com.example.tp_apirest.utils

object FormatUtils {
    fun formatoDuracion(segundos: Int?): String {
        if (segundos == null) return "-"
        val min = segundos / 60
        val seg = segundos % 60
        return String.format("%d:%02d", min, seg)
    }
}
