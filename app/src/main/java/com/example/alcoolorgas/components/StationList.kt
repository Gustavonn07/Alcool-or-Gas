package com.example.alcoolorgas.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alcoolorgas.R
import com.example.alcoolorgas.data.FuelHelpers
import com.example.alcoolorgas.models.FuelStation

@Composable
fun StationListPage(
    repo: FuelHelpers,
    onBack: () -> Unit,
    onSelect: (FuelStation) -> Unit,
    onDelete: (FuelStation) -> Unit = {},
    limit: Int,
    onLimitChange: (Int) -> Unit
) {
    val stations = remember { mutableStateOf(repo.getStations()) }
    var selectedStation by remember { mutableStateOf<FuelStation?>(null) }
    var showClearAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.back),
                fontSize = 18.sp,
                color = Color.Blue,
                modifier = Modifier.clickable { onBack() }
            )

            Row {
                Button(onClick = { onLimitChange(5) }, modifier = Modifier.padding(end = 4.dp)) {
                    Text("5")
                }
                Button(onClick = { onLimitChange(10) }, modifier = Modifier.padding(end = 4.dp)) {
                    Text("10")
                }
                Button(onClick = { onLimitChange(20) }, modifier = Modifier.padding(end = 4.dp)) {
                    Text("20")
                }
                Button(onClick = { onLimitChange(Int.MAX_VALUE) }) {
                    Text(stringResource(R.string.infinite))
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${stringResource(R.string.current_limit)}: ${
                    if (limit == Int.MAX_VALUE) "∞" else limit
                }",
                fontSize = 16.sp
            )

            Button(onClick = { showClearAll = true }) {
                Text(stringResource(R.string.clear_all))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(stations.value) { st ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .background(Color.LightGray)
                        .padding(16.dp)
                        .clickable {
                            selectedStation = st
                        }
                ) {
                    Text(st.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("${stringResource(R.string.alcohol)}: ${st.alcool}")
                    Text("${stringResource(R.string.gasoline)}: ${st.gasolina}")
                    Text("${stringResource(R.string.percent)}: ${st.percent}%")
                }
            }
        }

        if (selectedStation != null) {
            AlertDialog(
                onDismissRequest = { selectedStation = null },
                title = { Text(stringResource(R.string.choose_action)) },
                text = { Text(stringResource(R.string.select_action_desc)) },
                confirmButton = {
                    TextButton(onClick = {
                        onSelect(selectedStation!!)
                        selectedStation = null
                    }) {
                        Text(stringResource(R.string.edit))
                    }
                },
                dismissButton = {
                    Column {
                        TextButton(onClick = {
                            repo.deleteStation(selectedStation!!.id)
                            stations.value = repo.getStations()
                            onDelete(selectedStation!!)
                            selectedStation = null
                        }) {
                            Text(stringResource(R.string.delete), color = Color.Red)
                        }

                        TextButton(onClick = { selectedStation = null }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            )
        }

        if (showClearAll) {
            AlertDialog(
                onDismissRequest = { showClearAll = false },
                title = { Text(stringResource(R.string.clear_all_question)) },
                text = { Text(stringResource(R.string.clear_all_warning)) },
                confirmButton = {
                    TextButton(onClick = {
                        repo.clearAll()
                        stations.value = repo.getStations()
                        showClearAll = false
                    }) {
                        Text(stringResource(R.string.confirm), color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearAll = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
