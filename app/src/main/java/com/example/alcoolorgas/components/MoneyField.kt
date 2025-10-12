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
    TextField(
        onValueChange = { newValue ->
            val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }

            val dotCount = filtered.count { it == '.' }
            val commaCount = filtered.count { it == ',' }
            if (dotCount + commaCount > 1) return@TextField

            val normalized = filtered.replace(',', '.')

            setValue(normalized)
        },
        value = value.replace(",", "."),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = Modifier.fillMaxWidth()
    )
}