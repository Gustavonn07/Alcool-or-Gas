package com.example.alcoolorgas

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolorgas.components.MoneyField
import com.example.alcoolorgas.components.StationListPage
import com.example.alcoolorgas.data.FuelHelpers
import com.example.alcoolorgas.models.FuelStation
import com.example.alcoolorgas.ui.theme.AlcoolOrGasTheme
import com.example.alcoolorgas.ui.theme.Purple40
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    private val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION

    private fun requestLocationPermission() {
        if (checkSelfPermission(LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(LOCATION_PERMISSION), 100)
        }
    }

    fun getLastKnownLocation(): Location? {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkSelfPermission(LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = getSharedPreferences("alcool_gas_prefs", MODE_PRIVATE)
        val savedPercentSwitch = prefs.getBoolean("percentSwitch", true)
        val savedDarkTheme = prefs.getBoolean("darkTheme", false)
        requestLocationPermission()

        setContent {
            var isDarkTheme by remember { mutableStateOf(savedDarkTheme) }
            var percentChecked by remember { mutableStateOf(savedPercentSwitch) }

            var userLocation by remember {
                mutableStateOf(getLastKnownLocation())
            }

            AlcoolOrGasTheme(darkTheme = isDarkTheme) {
                AlcoolOrGas(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = {
                        isDarkTheme = it
                        prefs.edit().putBoolean("darkTheme", it).apply()
                    },
                    switchChecked = percentChecked,
                    onSwitchChange = {
                        percentChecked = it
                        prefs.edit().putBoolean("percentSwitch", it).apply()
                    },
                    userLocation = userLocation,
                )
            }
        }
    }
}

@Composable
fun AlcoolOrGas(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    switchChecked: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    userLocation: Location?
) {
    val context = LocalContext.current
    val repo = remember { FuelHelpers(context) }

    var currentPage by remember { mutableStateOf("home") }

    var alcoolPrice by remember { mutableStateOf("") }
    var gasPrice by remember { mutableStateOf("") }
    var station by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<String?>(null) }
    var maxStations by remember { mutableStateOf(Int.MAX_VALUE) }

    fun formatNumber(value: String): Double? {
        val cleaned = value.replace(Regex("[^0-9,.]"), "").replace(',', '.')
        return cleaned.toDoubleOrNull()
    }

    val backgroundColor = if (isDarkTheme) Color.DarkGray else Color.White
    val textColor = if (isDarkTheme) Color.White else Purple40
    val percentValue = if (switchChecked) 75 else 70

    when (currentPage) {

        "home" -> Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(42.dp)
            ) {
                Text(stringResource(id = R.string.dark_mode), color = textColor)
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) }
                )
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (editingId == null)
                        stringResource(R.string.title_new_station)
                    else
                        stringResource(R.string.title_edit_station),
                    color = textColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(64.dp))

                MoneyField(
                    value = alcoolPrice,
                    setValue = { alcoolPrice = it },
                    label = stringResource(R.string.label_alcool),
                    placeholder = stringResource(R.string.placeholder_price)
                )

                Spacer(modifier = Modifier.height(12.dp))

                MoneyField(
                    value = gasPrice,
                    setValue = { gasPrice = it },
                    label = stringResource(R.string.label_gas),
                    placeholder = stringResource(R.string.placeholder_price)
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    onValueChange = { station = it },
                    value = station,
                    label = { Text(stringResource(R.string.label_station_name), color = textColor) },
                    placeholder = { Text(stringResource(R.string.placeholder_station), color = textColor.copy(alpha = 0.5f)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.opt_70), color = textColor)
                    Switch(
                        checked = switchChecked,
                        onCheckedChange = { onSwitchChange(it) }
                    )
                    Text(stringResource(R.string.opt_75), color = textColor)

                    Button(
                        onClick = { currentPage = "list" },
                        modifier = Modifier.width(130.dp)
                    ) {
                        Text(stringResource(R.string.see_list))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        val alcool = formatNumber(alcoolPrice) ?: 0.0
                        val gas = formatNumber(gasPrice) ?: 0.0

                        if (alcool == 0.0 || gas == 0.0 || station.isBlank()) {
                            result = context.getString(R.string.error_fill_all)
                            return@Button
                        }

                        val lat = userLocation?.latitude
                        val lng = userLocation?.longitude

                        if (editingId == null) {
                            repo.addStation(
                                FuelStation(
                                    id = java.util.UUID.randomUUID().toString(),
                                    name = station,
                                    alcool = alcool,
                                    gasolina = gas,
                                    percent = percentValue,
                                    latitude = lat,
                                    longitude = lng
                                )
                            )
                        } else {
                            repo.updateStation(
                                FuelStation(
                                    id = editingId!!,
                                    name = station,
                                    alcool = alcool,
                                    gasolina = gas,
                                    percent = percentValue,
                                    latitude = lat,
                                    longitude = lng
                                )
                            )
                            editingId = null
                        }

                        alcoolPrice = ""
                        gasPrice = ""
                        station = ""
                        result = context.getString(R.string.saved_station)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (editingId == null)
                            stringResource(R.string.btn_save)
                        else
                            stringResource(R.string.btn_update)
                    )
                }

                if (result.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = result, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        "list" -> StationListPage(
            repo = repo,
            onBack = { currentPage = "home" },
            onSelect = { st ->
                alcoolPrice = st.alcool.toString()
                gasPrice = st.gasolina.toString()
                onSwitchChange(st.percent == 75)
                station = st.name
                editingId = st.id
                currentPage = "home"
            },
            limit = maxStations,
            onLimitChange = { maxStations = it }
        )
    }
}