package com.forecast.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mahshad.common.model.datasource.models.forecast.Hour
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTextApi::class) // For rememberTextMeasurer
@Composable
fun TemperatureCurveChart(
    hours: List<Hour>,
    modifier: Modifier = Modifier,
    // You might want to pass these colors from your theme
    curveStartColor: Color = Color(0xFFFFCC80), // Light orange
    curveEndColor: Color = Color(0xFFFFECB3),   // Lighter orange
    indicatorColor: Color = Color.Gray.copy(alpha = 0.6f),
    lineColor: Color = Color.LightGray.copy(alpha = 0.5f)
) {
    if (hours.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No hourly data available")
        }
        return
    }

    // State for the currently selected hour index
    var selectedHourIndex by remember { mutableStateOf(0) }

    // Text Measurer for measuring text dimensions
    val textMeasurer = rememberTextMeasurer()

    Box(modifier = modifier.fillMaxWidth().height(200.dp)) { // Adjust height as needed
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding for labels
                .pointerInput(hours) { // Recompose pointerInput if hours list changes
                    detectDragGestures(
                        onDragEnd = { /* Optional: Snap to closest point on drag end */ },
                        onDragCancel = { /* Optional */ },
                        onDragStart = { offset ->
                            val density = density
                            val availableWidth = size.width.toFloat()
                            val step = availableWidth / (hours.size - 1).toFloat()
                            val newIndex = ((offset.x - (step / 2)) / step).roundToInt()
                                .coerceIn(0, hours.size - 1)
                            selectedHourIndex = newIndex
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val density = density
                            val availableWidth = size.width.toFloat()
                            val step = availableWidth / (hours.size - 1).toFloat()
                            val newIndex = ((change.position.x - (step / 2)) / step).roundToInt()
                                .coerceIn(0, hours.size - 1)
                            selectedHourIndex = newIndex
                        }
                    )
                }
        ) {
            val availableWidth = size.width
            val availableHeight = size.height

            // Calculate min/max temperature for scaling
            val minTemp = hours.minOfOrNull { it.tempC } ?: 0.0
            val maxTemp = hours.maxOfOrNull { it.tempC } ?: 1.0
            val tempRange = maxTemp - minTemp

            // Calculate positions for each hour
            val points = hours.mapIndexed { index, hour ->
                val x = (index.toFloat() / (hours.size - 1).toFloat()) * availableWidth
                // Normalize tempC to 0-1 range and then scale to height
                val y = availableHeight - (((hour.tempC - minTemp) / tempRange).toFloat() * availableHeight)
                Offset(x, y)
            }

            // --- Draw Horizontal Grid Lines (e.g., for temperature) ---
            val numHorizontalLines = 4 // Adjust as needed
            for (i in 0 until numHorizontalLines) {
                val y = availableHeight * (i.toFloat() / (numHorizontalLines - 1))
                drawLine(
                    color = lineColor,
                    start = Offset(0f, y),
                    end = Offset(availableWidth, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // --- Draw the Curved Line ---
            val path = Path()
            if (points.isNotEmpty()) {
                path.moveTo(points.first().x, points.first().y)
                for (i in 0 until points.size - 1) {
                    val p1 = points[i]
                    val p2 = points[i + 1]
                    // Calculate control points for a smooth curve (Catmull-Rom-like)
                    // For simplicity, using quadratic Bézier curves here.
                    // For more accurate spline, you might need a different algorithm or library.
                    path.quadraticBezierTo(
                        x1 = (p1.x + p2.x) / 2f,
                        y1 = p1.y,
                        x2 = p2.x,
                        y2 = p2.y
                    )
                }

                // Create a gradient brush for the curve
                val gradientBrush = Brush.horizontalGradient(
                    colors = listOf(curveStartColor, curveEndColor),
                    startX = 0f,
                    endX = availableWidth
                )

                drawPath(
                    path = path,
                    brush = gradientBrush,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // --- Draw the movable indicator ---
            if (points.isNotEmpty() && selectedHourIndex < points.size) {
                val selectedPoint = points[selectedHourIndex]

                // Draw vertical dashed line
                drawLine(
                    color = indicatorColor,
                    start = Offset(selectedPoint.x, 0f),
                    end = Offset(selectedPoint.x, availableHeight),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // Draw horizontal dashed line
                drawLine(
                    color = indicatorColor,
                    start = Offset(0f, selectedPoint.y),
                    end = Offset(availableWidth, selectedPoint.y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )

                // Draw the circle on the curve
                drawCircle(
                    color = Color.White,
                    radius = 8.dp.toPx(),
                    center = selectedPoint,
                    style = Stroke(width = 2.dp.toPx()) // Example border
                )
//                drawCircle(
//                    color = , // Filled circle
//                    radius = 6.dp.toPx(),
//                    center = selectedPoint
//                )

                // Draw "X" or other indicator at the bottom for the selected time
                val xOffsetForX = selectedPoint.x
                val xAxisY = availableHeight
                val xSize = 10.dp.toPx()
                drawLine(
                    color = indicatorColor,
                    start = Offset(xOffsetForX - xSize / 2, xAxisY - xSize / 2),
                    end = Offset(xOffsetForX + xSize / 2, xAxisY + xSize / 2),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = indicatorColor,
                    start = Offset(xOffsetForX - xSize / 2, xAxisY + xSize / 2),
                    end = Offset(xOffsetForX + xSize / 2, xAxisY - xSize / 2),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        // --- Time Labels at the Bottom ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp), // Adjust padding if needed
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val formatter = remember { DateTimeFormatter.ofPattern("h a", Locale.ENGLISH) }
            hours.forEachIndexed { index, hour ->
                // Only show a few labels for clarity, e.g., every 3 hours or a fixed number
                if (index % (hours.size / 4).coerceAtLeast(1) == 0 || index == hours.size -1) { // Shows approx 4 labels
                    val timeString = try {
                        LocalDateTime.parse(hour.time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).format(formatter)
                    } catch (e: Exception) {
                        hour.time.takeLast(5) // Fallback: "HH:mm" from "YYYY-MM-DD HH:MM"
                    }

                    val textColor = if (index == selectedHourIndex) MaterialTheme.colorScheme.primary else Color.Gray
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor,
                        modifier = Modifier.weight(1f), // Distribute space evenly
                        textAlign = TextAlign.Center
                    )
                } else {
                    Spacer(Modifier.weight(1f)) // Maintain spacing for skipped labels
                }
            }
        }
    }
}

// Example Usage:
@Composable
fun PreviewTemperatureChart() {
    MaterialTheme {
        //val dummyHours = remember { generateDummyHourlyData() }
        //TemperatureCurveChart(hours = dummyHours)
    }
}