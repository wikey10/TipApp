package com.app.tipapp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.tipapp.components.InputField
import com.app.tipapp.ui.theme.TipappTheme
import com.app.tipapp.utils.calculateTotalTip
import com.app.tipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = Color(0XFFBB86FC).toArgb()
        setContent {
            MyApp()

        }
    }
}

@Composable
fun MyApp() {
    TipappTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Surface(color = MaterialTheme.colorScheme.background) {
                Box(modifier = Modifier.fillMaxSize()
                    .padding(innerPadding).padding(10.dp)){
                    Column {
                        Spacer(
                            Modifier.height(20.dp)
                        )
                        MainContent()
                    }

                }
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun ToHeader(totalPerPerson:Double= 134.0){
        Surface(modifier = Modifier.fillMaxWidth().height(150.dp).clip(shape = CircleShape.copy(all = CornerSize(12.dp))), color = Color(0XFFE9D7F7)) {
         Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center) {
             val total = "%.2f".format(totalPerPerson)
             Text("Total Per Person", style = MaterialTheme.typography.titleLarge)
             Text("$$total", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
         }
    }
}

@Composable
fun MainContent(){
    BillForm {
        billAmt-> Log.d("AMT","MainContentvALUE :$billAmt")
    }
}

@Composable
fun BillForm(modifier: Modifier=Modifier,onValChange:(String)->Unit={}){
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val sampleValue = remember {
        mutableIntStateOf(value = 1)
    }
    val keyBoardController = LocalSoftwareKeyboardController.current

    val  sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }

    val  tipPercentage = (sliderPositionState.floatValue *100).toInt()

    val range = IntRange(start = 1, endInclusive = 10)

    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    ToHeader(totalPerPerson = totalPersonState.doubleValue)
    Spacer(
        Modifier.height(20.dp)
    )
    Surface(modifier = Modifier.padding(2.dp), shape = RoundedCornerShape(
        corner = CornerSize(8.dp )
    ), border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
            ) {
            InputField(
                valueState = totalBillState,
                enabled = true,
                labelId = "Enter Bill",
                isSingleLine = true,
                onAction = KeyboardActions{
                   if(!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyBoardController?.hide()
                }
            )
            if(validState){
                Row(modifier = Modifier.padding(3.dp
                    ), horizontalArrangement = Arrangement.Start) {
                    Text("Split", modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    ))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {
                        RoundIconButton(
                            modifier = modifier,
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                removeValue(values = sampleValue.intValue) {
                                    updatedValue-> sampleValue.intValue = updatedValue
                                }
                                totalPersonState.doubleValue = calculateTotalPerson(totalBill = totalBillState.value.toDouble(), splitBy = sampleValue.intValue, tipPercentage = tipPercentage)
                            }
                        )
                        Text("${sampleValue.intValue}", modifier = modifier.align(alignment = Alignment.CenterVertically).padding(start = 5.dp, end = 5.dp))
                        RoundIconButton(
                            modifier = modifier,
                            imageVector = Icons.Default.Add,
                            onClick = {
                                if(sampleValue.intValue <range.last){
                                    addValue(values = sampleValue.intValue) { updatedValue ->
                                        sampleValue.intValue = updatedValue
                                    }
                                    totalPersonState.doubleValue = calculateTotalPerson(totalBill = totalBillState.value.toDouble(), splitBy = sampleValue.intValue, tipPercentage = tipPercentage)
                                }
                            }
                        )
                    }
                }

            //TIP ROW
            Row(modifier = Modifier.padding(2.dp)) {
                Text(text = "Tip", modifier = modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(170.dp))
                Text("${tipAmountState.doubleValue}", modifier = modifier.align(alignment = Alignment.CenterVertically))
            }
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text("$tipPercentage %")
                Spacer(modifier = modifier.height(20.dp))
                Slider(
                    modifier =Modifier.padding(start = 15.dp,end=15.dp),
                    onValueChangeFinished = {
                        tipAmountState.doubleValue = calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                            tipPercentage =
                            tipPercentage)
                        totalPersonState.doubleValue = calculateTotalPerson(totalBill = totalBillState.value.toDouble(), splitBy = sampleValue.intValue, tipPercentage = tipPercentage)
                    },
                    value = sliderPositionState.floatValue, onValueChange = {
                    newVal->
                    sliderPositionState.floatValue = newVal
                        tipAmountState.doubleValue = calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                         tipPercentage =
                        tipPercentage)
                        totalPersonState.doubleValue = calculateTotalPerson(totalBill = totalBillState.value.toDouble(), splitBy = sampleValue.intValue, tipPercentage = tipPercentage)
                    Log.d("Slider","BillForm: $newVal")
                })
            }
            }
            else{
                Box {}
            }
        }
    }
}

fun addValue(values: Int = 0, updateValue: (Int) -> Unit) {
    updateValue(values + 1)
}

fun removeValue(values: Int = 0, updateValue: (Int) -> Unit) {
    if(values<=1){
        updateValue(1)
    }
    else{
        updateValue(values - 1)
    }
}

fun calculateTotalPerson(totalBill: Double,splitBy:Int,tipPercentage: Int):Double{
    val  bill = calculateTotalTip(totalBill = totalBill,tipPercentage= tipPercentage) + totalBill
    return  (bill/splitBy)
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipappTheme {
        MyApp()
    }
}