package pt.ipg.projetoprogramacao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.ipg.projetoprogramacao.ui.theme.ProjetoProgramacaoTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment



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
fun ObjectiveCalculatorLayout(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Cálculo do Objetivo",
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}