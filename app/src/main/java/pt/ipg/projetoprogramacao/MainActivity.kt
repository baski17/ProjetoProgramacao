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
        onValueChange = { newValue ->
            // Verifica se o novo valor contém apenas dígitos ou está vazio
            if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
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
    var salaryInput by rememberSaveable { mutableStateOf("") }   //variavel usada para armazenar o valor do salario introduzido,rememberSaveable é para guardar o valor caso a tela rode ou outras situações e é inicializou com uma string vazia
    var expensesInput by rememberSaveable { mutableStateOf("") } //""
    var goalInput by rememberSaveable { mutableStateOf("") }     //""
    var showNecessaryAmount by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    val salary = salaryInput.toDoubleOrNull() ?: 0.0
    val expenses = expensesInput.toDoubleOrNull() ?: 0.0
    val goal = goalInput.toDoubleOrNull() ?: 0.0

    val monthlySavings = salary - expenses                             //calculo das poupancas mensais (salario-despesas)
    val dailySavings = monthlySavings / 30                             //calculo das poupansas diarias considerando 30 dias
    val necessaryAmount = goal - monthlySavings                        //calculo do dinheiro nessecario para alcancar o objetivo (objetivo-poupancas mensais)
    val daysToGoal = if (dailySavings > 0) goal / dailySavings else Double.POSITIVE_INFINITY //se as poupancas diarias for maior que 0 , objetivo / poupancas diarias  que nos da os dias nessecarios para atingir o objetivo se for <0 aparece mensagem de erro

    Box(                                                               //usei uma box para conseguir meter a imagem por tras de tudo senao nao consegui fazer isso com coluna
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.money),          // Replace with your image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().alpha(0.3f)

        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())                  //para conseguir dar scroll,é mais util quando o telemovel esta na horizontal
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,         //centralizar horizontalmente
            verticalArrangement = Arrangement.Center                    //centralizar verticalmente
        ) {
            Text(
                text = "Cálculo do Objetivo",
                modifier = Modifier
                    .padding(bottom = 16.dp)                            //espacamento em relacao a linha debaixo
                    .align(alignment = Alignment.Start),                //alinhamento do titulo á esquerda
                    fontSize = 24.sp                                    //tamanho da letra

            )

            EditNumberField(                                            //um campo editavel para introduzir o salario
                labelText = "Salário",
                value = salaryInput,                                    //o valor introduzido é um valor que fica na variavel salaryInput
                onValueChange = { newValue -> salaryInput = newValue }, //a variavel vai ter o valor do que for introduzido
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            EditNumberField(                                            //um campo editavel para introduzir as despesas
                labelText = "Despesas",
                value = expensesInput,                                  //o valor introduzido fica guardado nesta variavel
                onValueChange = { newValue -> expensesInput = newValue }, //a variavel vai ter o valor do valor introduzido
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            EditNumberField(                                            //um campo editavel para introduzir o valor a alcancar
                labelText = "Valor Objetivo",
                value = goalInput,                                      //o valor introduzido fica guardado nesta variavel
                onValueChange = { newValue -> goalInput = newValue },   //a variavel vai ter o valor que for introduzido no campo
                action = ImeAction.Done,                                //quando clicar enter passa para o proximo campo
                modifier = Modifier
                    .fillMaxWidth()                                     //para preencher toda a largura do telemovel
                    .padding(bottom = 16.dp)                            //espacamento em relacao a parte de baixo
            )

            Button(
                onClick = {

                    showError = goal <= salary                          //se o objetivo for menor que o salario aparece uma mensagem de erro a dizer que ja foi comprido o objetivo
                    showNecessaryAmount = !showError                    //se o objetivo for maior que o salario mostra o dinheiro nessecario
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Calcular Valor")
            }

            if (showNecessaryAmount) {                                  //caso seja para mostra o dinheiro nessecario
                Text(
                    text = "Dinheiro necessário: $necessaryAmount",
                    style = MaterialTheme.typography.bodyLarge,         //estilo de letra
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)            //espacamento em relacao a linha de cima
                )
                if (daysToGoal.isFinite()) {                            //se os dias para atingir o objetivo for finito
                    Text(
                        text = "Tempo necessário: %.0f dias".format(daysToGoal), //aparece a informacao da variavel daysTogoal
                        style = MaterialTheme.typography.bodyLarge,    //estilo de letra
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

            if(showError) {
                Text(
                    text = "O seu objetivo foi cumprido",
                    color = Color.Red,                          //com a cor vermelha
                    style = MaterialTheme.typography.bodyLarge, //estilo de letra
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

        }
    }
}
