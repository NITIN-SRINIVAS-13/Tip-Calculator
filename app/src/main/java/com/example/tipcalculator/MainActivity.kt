package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import components.InputField
import util.calculateTotalPerPerson
import util.calculateTotalTip
import widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                Column(modifier = Modifier.systemBarsPadding()) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MyApp(content : @Composable () -> Unit) {
    TipCalculatorTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson : Double = 0.0){
    Surface(modifier = Modifier.fillMaxWidth().padding(15.dp).height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)){
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            val total = "%.2f".format(totalPerPerson)
            Text("Total Per Person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text("$$total",
                style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold)
            )
        }
    }
}

@Composable
fun MainContent(modifier : Modifier = Modifier,
             onValChange : (String) -> Unit = {}
){
    val totalBillState = remember {mutableStateOf("")}
    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val splitState = remember{ mutableIntStateOf(1)}
    var sliderPositionState by remember { mutableFloatStateOf(0f) }
    val tipPercentage = (sliderPositionState * 100).toInt()
    val range = IntRange(start = 1, endInclusive = 100)
    var tipAmountState by remember { mutableDoubleStateOf(0.0) }
    var totalPerPersonState by remember { mutableDoubleStateOf(0.0) }
    Column{
        TopHeader(totalPerPersonState)
        Spacer(modifier = modifier.height(15.dp))

        Surface(
            modifier = modifier.padding(2.dp).fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(1.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (validState) {
                            keyboardController?.hide()
                        }
                    }
                )
                if (validState) {
                    tipAmountState = calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)
                    totalPerPersonState = calculateTotalPerPerson(totalBillState.value.toDouble(), splitState.intValue, tipPercentage)
                    Row(
                        modifier = modifier.padding(3.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Split", modifier = modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = modifier.width(120.dp))
                        Row(
                            modifier = modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitState.intValue =
                                        if (splitState.intValue > 1) splitState.intValue - 1
                                        else 1
                                }
                            )
                            Text(
                                "${splitState.intValue}",
                                modifier = Modifier.align(Alignment.CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )
                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = {
                                    if (splitState.intValue < range.last) {
                                        splitState.intValue = splitState.intValue + 1
                                    }
                                }
                            )
                        }
                    }
                    Row(modifier = modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                        Text("Tip", modifier = modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = modifier.width(200.dp))
                        Text("$%.2f".format(tipAmountState), modifier = modifier.align(Alignment.CenterVertically))
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("$tipPercentage %")
                        Spacer(modifier = modifier.height(14.dp))
                        // Slider
                        Slider(
                            value = sliderPositionState, onValueChange = { newValue -> sliderPositionState = newValue },
                            modifier = modifier.padding(start = 16.dp, end = 16.dp),
                            //steps = 4,
                            onValueChangeFinished = {})
                    }
                }
            }
        }
    }
}


