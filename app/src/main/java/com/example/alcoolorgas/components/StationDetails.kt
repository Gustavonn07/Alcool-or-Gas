//package com.example.alcoolorgas.components
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.alcoolorgas.models.FuelStation
//
//@Composable
//fun StationDetailScreen(station: FuelStation) {
//    Column(Modifier.padding(16.dp)) {
//        Text("Posto: ${station.name}", fontSize = 22.sp)
//        Text("Álcool: ${station.alcool}")
//        Text("Gasolina: ${station.gasolina}")
//        Text("Registrado em: ${Date(station.date)}")
//
//        Spacer(Modifier.height(20.dp))
//
//        if (station.latitude != null && station.longitude != null) {
//            GoogleMap(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp),
//                cameraPositionState = rememberCameraPositionState {
//                    position = CameraPosition.fromLatLngZoom(
//                        LatLng(station.latitude, station.longitude), 16f
//                    )
//                }
//            ) {
//                Marker(
//                    state = MarkerState(position = LatLng(station.latitude, station.longitude)),
//                    title = station.name
//                )
//            }
//        }
//    }
//}
