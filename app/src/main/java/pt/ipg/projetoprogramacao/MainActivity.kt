package pt.ipg.projetoprogramacao

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipg.projetoprogramacao.ui.theme.ProjetoProgramacaoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetoProgramacaoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ObjectiveCalculatorLayout(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun EditNumberField(
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    action: ImeAction = ImeAction.Next,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = labelText) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = action
        ),
        modifier = modifier
    )
}

@Composable
fun ObjectiveCalculatorLayout(modifier: Modifier = Modifier) {
    var salaryInput by rememberSaveable { mutableStateOf("") }
    var expensesInput by rememberSaveable { mutableStateOf("") }
    var goalInput by rememberSaveable { mutableStateOf("") }
    var showNecessaryAmount by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    val salary = salaryInput.toDoubleOrNull() ?: 0.0
    val expenses = expensesInput.toDoubleOrNull() ?: 0.0
    val goal = goalInput.toDoubleOrNull() ?: 0.0

    val monthlySavings = salary - expenses
    val dailySavings = monthlySavings / 30 // Considering an average month length
    val necessaryAmount = goal - monthlySavings
    val daysToGoal = if (dailySavings > 0) goal / dailySavings else Double.POSITIVE_INFINITY

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.money), // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.3f)

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cálculo do Objetivo",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.Start),
                    fontSize = 24.sp

            )

            EditNumberField(
                labelText = "Salário",
                value = salaryInput,
                onValueChange = { newValue -> salaryInput = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            EditNumberField(
                labelText = "Despesas",
                value = expensesInput,
                onValueChange = { newValue -> expensesInput = newValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            EditNumberField(
                labelText = "Valor Objetivo",
                value = goalInput,
                onValueChange = { newValue -> goalInput = newValue },
                action = ImeAction.Done,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    showError = goal <= salary
                    showNecessaryAmount = !showError
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Calcular Valor")
            }

            if (showNecessaryAmount) {
                Text(
                    text = "Dinheiro necessário: $necessaryAmount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
                if (daysToGoal.isFinite()) {
                    Text(
                        text = "Tempo necessário: %.0f dias".format(daysToGoal),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Text(
                        text = "Tempo necessário: Impossível atingir o objetivo com as economias atuais",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            if (showError) {
                Text(
                    text = "O seu objetivo foi cumprido",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
