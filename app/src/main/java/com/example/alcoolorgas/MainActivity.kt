package com.example.alcoolorgas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolorgas.components.MoneyField
import com.example.alcoolorgas.ui.theme.AlcoolOrGasTheme
import com.example.alcoolorgas.ui.theme.Purple40

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            AlcoolOrGasTheme(darkTheme = isDarkTheme) {
                AlcoolOrGas(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it }
                )
            }
        }
    }
}

@Composable
fun AlcoolOrGas(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    fun formatNumber(value: String): Double? {
        // Remove tudo que não for número, vírgula ou ponto
        val cleaned = value.replace(Regex("[^0-9,.]"), "")
            .replace(',', '.')
        return cleaned.toDoubleOrNull()
    }

    var alcoolPrice by remember { mutableStateOf("") }
    var gasPrice by remember { mutableStateOf("") }
    var station by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("") }

    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val textColor = if (isDarkTheme) Color.White else Purple40
    val percent = if (checked) 0.75 else 0.70

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(backgroundColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(42.dp)
        ) {
            Text("Modo Escuro", color = textColor)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange(it) }
            )
        }
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Álcool ou Gasolina?",
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(64.dp))

            MoneyField(
                value = alcoolPrice,
                setValue = { newValue -> alcoolPrice = newValue },
                label = "Preço do Álcool (R$)",
                placeholder = "Ex: 5.49"
            )

            Spacer(modifier = Modifier.height(12.dp))

            MoneyField(
                value = gasPrice,
                setValue = { newValue -> gasPrice = newValue },
                label = "Preço da Gasolina (R$)",
                placeholder = "Ex: 5.49"
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                onValueChange = { newStation -> station = newStation },
                value = station,
                label = { Text("Nome do Posto (Opcional)", color = textColor) },
                placeholder = { Text("Ex: Posto do Pici", color = textColor.copy(alpha = 0.5f)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("70%", color = textColor)
                Spacer(modifier = Modifier.width(12.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = { checked = it },
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("75%", color = textColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val alcool = formatNumber(alcoolPrice) ?: 0.0
                    val gas = formatNumber(gasPrice) ?: 0.0
                    result = if (alcool == 0.0 || gas == 0.0) {
                        "Preencha os preços corretamente!"
                    } else if (alcool / gas <= percent) {
                        "Abasteça com Álcool"
                    } else {
                        "Abasteça com Gasolina"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                val color = when {
                    result.contains("Álcool") -> Color(0xFF009688)
                    result.contains("Gasolina") -> Color(0xFF3F51B5)
                    else -> Color.Gray
                }
                Text(
                    text = result,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
