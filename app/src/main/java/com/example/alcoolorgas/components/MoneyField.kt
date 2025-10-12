package com.example.alcoolorgas.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MoneyField(
    value: String,
    setValue: (String) -> Unit,
    label: String,
    placeholder: String
) {
    fun formatCurrency(value: Double): String {
        val receivedValueSeparated = "%.2f".format(value).split(".")
        val real = receivedValueSeparated[0]
        val cent = receivedValueSeparated[1]
        return "R$ ${real},${cent}"
    }

    TextField(
        onValueChange = { newValue ->
            // Filtra qualquer coisa que não seja um dígito, uma virgula ou um ponto com o Regex
            val cleanedValue = newValue.replace(Regex("[^0-9,.]"), "")
            val numericValue = cleanedValue.replace(',', '.').toDoubleOrNull()

            val formattedValue =
                if (numericValue != null && cleanedValue.contains(',')) {
                    formatCurrency(numericValue)
                } else {
                    cleanedValue
                }

            setValue(formattedValue)
        },
        value = value,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
}