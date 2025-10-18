package com.forecast.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.mahshad.common.model.datasource.models.forecast.Hour
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

private val Y_AXIS_WIDTH = 40.dp
private val CHART_HEIGHT = 200.dp
private val MIN_POINT_WIDTH = 60.dp // Minimum spacing between points


@Composable
fun YAxisLabels(
    minTemp: Double,
    maxTemp: Double,
    modifier: Modifier = Modifier
) {
    // This must match the 'numHorizontalLines' constant used in the Canvas (4 in the main function)
    val numLabels = 4
    // Calculate the temperature difference between each label
    val tempStep = (maxTemp - minTemp) / (numLabels - 1).coerceAtLeast(1)

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End // Align text to the right edge of its container
    ) {
        // Top label (Max Temp)
        Text(
            // Use roundToInt() to display clean integer temperatures, which is common for charts
            text = "${maxTemp.roundToInt()}°",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            textAlign = TextAlign.End
        )

        // Middle labels
        for (i in 1 until numLabels - 1) {
            // Calculate the temperature value for the current grid line
            val temp = maxTemp - (i * tempStep)
            Text(
                text = "${temp.roundToInt()}°",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                textAlign = TextAlign.End
            )
        }

        // Bottom label (Min Temp)
        Text(
            text = "${minTemp.roundToInt()}°",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTextApi::class)
@Composable
fun TemperatureCurveChart(
    hours: List<Hour>,
    modifier: Modifier = Modifier,
    curveStartColor: Color = Color(0xFFFFCC80),
    curveEndColor: Color = Color(0xFFFFECB3),
    indicatorColor: Color = Color.Gray.copy(alpha = 0.6f),
    lineColor: Color = Color.LightGray.copy(alpha = 0.5f),
    dotColor: Color = MaterialTheme.colorScheme.primary
) {
    if (hours.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No hourly data available")
        }
        return
    }

    // 1. STATE INITIALIZATION (Find Current Hour)

    // Get the current system hour for initial placement
    val currentSystemHour = remember { LocalTime.now().hour }

    val initialIndex = remember(hours) {
        // Find the index of the first hour in the list that matches the current system hour.
        hours.indexOfFirst { hour ->
            try {
                val hourTime = LocalDateTime.parse(
                    hour.time,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
                hourTime.hour == currentSystemHour
            } catch (e: Exception) {
                // Return false if parsing fails
                false
            }
        }
            // If not found (returns -1), default to 0 (the first hour)
            .coerceAtLeast(0)
    }

    // State for the currently selected hour index
    var selectedHourIndex by remember { mutableStateOf(initialIndex) }

    // Non-state variables
    val minTemp = hours.minOfOrNull { it.tempC } ?: 0.0
    val maxTemp = hours.maxOfOrNull { it.tempC } ?: 1.0
    val tempRange = (maxTemp - minTemp).coerceAtLeast(1.0)
    val totalHours = hours.size
    val scrollState = rememberScrollState()
    val density = LocalDensity.current


    // Use BoxWithConstraints to safely get the parent's maximum width for layout calculation
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(CHART_HEIGHT + 40.dp)
            .padding(top = 8.dp)
    ) {
        // Calculate the necessary width for the canvas:
        val calculatedWidth: Dp = (totalHours * MIN_POINT_WIDTH)
        // The canvas width is the calculated width, or the parent's maxWidth, whichever is greater
        val canvasWidth: Dp = calculatedWidth.coerceAtLeast(this.maxWidth)

        // 2. INITIAL SCROLL EFFECT (Center the initialIndex)

        // This effect runs once when the component first enters the composition
        LaunchedEffect(key1 = initialIndex, scrollState, totalHours) {
            // Only scroll if we found a valid index or if the list isn't tiny
            if (initialIndex >= 0 && totalHours > 1) {
                val minPointWidthPx = with(density) { MIN_POINT_WIDTH.toPx() }
                val viewportWidthPx = with(density) { maxWidth.toPx() }

                // Calculate the pixel position where the selected point is centered
                val pointCenterPosition = initialIndex * minPointWidthPx

                // Target scroll position = Point position - (Half of the visible screen width)
                val targetScroll = (pointCenterPosition - (viewportWidthPx / 2f))
                    // Ensure scroll position is within valid bounds
                    .coerceIn(0f, scrollState.maxValue.toFloat())
                    .roundToInt()

                // Use animateScrollTo for a smooth scroll transition
                scrollState.animateScrollTo(targetScroll)
            }
        }


        Row(modifier = Modifier.fillMaxSize()) {
            // --- Temperature Labels (Y-Axis) ---
            YAxisLabels(
                minTemp = minTemp,
                maxTemp = maxTemp,
                modifier = Modifier
                    .width(Y_AXIS_WIDTH)
                    .fillMaxHeight()
                    .padding(end = 4.dp, top = 8.dp, bottom = 40.dp)
            )

            // --- Scrollable Chart Area ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                Box(modifier = Modifier
                    .width(canvasWidth) // Set the calculated width for the scrollable content
                    .height(CHART_HEIGHT)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(hours, canvasWidth) {
                                // ... (Interaction logic for drag/tap remains here) ...
                                val availableWidth = size.width.toFloat()
                                val step = availableWidth / (hours.size - 1).coerceAtLeast(1).toFloat()

                                detectDragGestures(
                                    onDragStart = { offset ->
                                        selectedHourIndex = (offset.x / step).roundToInt()
                                            .coerceIn(0, hours.size - 1)
                                    },
                                    onDrag = { change, _ ->
                                        change.consume()
                                        selectedHourIndex = (change.position.x / step).roundToInt()
                                            .coerceIn(0, hours.size - 1)
                                    }
                                )
                            }
                    ) {
                        val availableWidth = size.width
                        val availableHeight = size.height
                        val numHorizontalLines = 4

                        // Calculate positions
                        val points = hours.mapIndexed { index, hour ->
                            val x = (index.toFloat() / (hours.size - 1).coerceAtLeast(1).toFloat()) * availableWidth
                            val y = availableHeight - (((hour.tempC - minTemp) / tempRange).toFloat() * availableHeight)
                            Offset(x, y)
                        }

                        // Draw Horizontal Grid Lines
                        for (i in 0 until numHorizontalLines) {
                            val y = availableHeight * (i.toFloat() / (numHorizontalLines - 1))
                            drawLine(
                                color = lineColor,
                                start = Offset(0f, y),
                                end = Offset(availableWidth, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Draw the Curved Line
                        val path = Path()
                        if (points.isNotEmpty()) {
                            path.moveTo(points.first().x, points.first().y)
                            for (i in 0 until points.size - 1) {
                                val p1 = points[i]
                                val p2 = points[i + 1]
                                path.quadraticBezierTo(
                                    x1 = (p1.x + p2.x) / 2f,
                                    y1 = p1.y,
                                    x2 = p2.x,
                                    y2 = p2.y
                                )
                            }
                            val gradientBrush = Brush.horizontalGradient(
                                colors = listOf(curveStartColor, curveEndColor),
                                startX = 0f,
                                endX = availableWidth
                            )
                            drawPath(path = path, brush = gradientBrush, style = Stroke(width = 3.dp.toPx()))
                        }

                        // Draw the movable indicator
                        if (points.isNotEmpty() && selectedHourIndex < points.size) {
                            val selectedPoint = points[selectedHourIndex]
                            // Vertical dashed line
                            drawLine(
                                color = indicatorColor,
                                start = Offset(selectedPoint.x, 0f),
                                end = Offset(selectedPoint.x, availableHeight),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            // Horizontal dashed line
                            drawLine(
                                color = indicatorColor,
                                start = Offset(0f, selectedPoint.y),
                                end = Offset(availableWidth, selectedPoint.y),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            // Draw the circle on the curve
                            drawCircle(color = dotColor, radius = 6.dp.toPx(), center = selectedPoint)
                            drawCircle(color = Color.White, radius = 8.dp.toPx(), center = selectedPoint, style = Stroke(width = 2.dp.toPx()))
                        }
                    }
                }

                // --- Time Labels at the Bottom ---
                Row(
                    modifier = Modifier
                        .width(canvasWidth)
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val formatter = remember { DateTimeFormatter.ofPattern("h a", Locale.ENGLISH) }
                    hours.forEachIndexed { index, hour ->
                        val showLabel = index % 3 == 0 || index == hours.size - 1

                        if (showLabel) {
                            val timeString = try {
                                LocalDateTime.parse(hour.time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).format(formatter)
                            } catch (e: Exception) {
                                hour.time.takeLast(5)
                            }

                            val textColor = if (index == selectedHourIndex) MaterialTheme.colorScheme.primary else Color.Gray

                            Text(
                                text = timeString,
                                style = MaterialTheme.typography.labelSmall,
                                color = textColor,
                                modifier = Modifier
                                    .width(MIN_POINT_WIDTH)
                                    .align(Alignment.Top),
                                textAlign = TextAlign.Center
                            )
                        } else if (index != hours.size - 1) {
                            Spacer(Modifier.width(MIN_POINT_WIDTH))
                        }
                    }
                }
            }
        }
    }}