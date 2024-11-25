package com.example.projetofeed

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetofeed.models.database.AppDatabase
import com.example.projetofeed.viewmodel.UsuarioViewModel
import com.example.projetofeed.viewmodel.UsuarioViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.google.gson.Gson
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.projetofeed.models.entity.Post
import com.example.projetofeed.viewmodel.PostViewModel
import com.example.projetofeed.viewmodel.PostViewModelFactory
import kotlinx.coroutines.launch

data class IdUsuario(val idUsuario : Int)

class MainActivity : ComponentActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).usuarioDao()
        UsuarioViewModelFactory(dao)
    }

    private val postViewModel: PostViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).postDao()
        PostViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(usuarioViewModel, postViewModel)
        }
    }

}

@Composable
fun AppNavigation(usuarioViewModel: UsuarioViewModel, postViewModel: PostViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") { MainScreen(navController, usuarioViewModel) }
        composable("Cadastro") { CadastroScreen(navController, usuarioViewModel) }
        composable("CriarPost/{idUsuario}") { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
            if (idUsuario != null) {
                CreatePostScreen(navController, postViewModel, idUsuario)
            }
        }
        composable("ListarPost/{idUsuario}") { backStackEntry ->
            val idUsuario = backStackEntry.arguments?.getString("idUsuario")?.toIntOrNull()
            if (idUsuario != null) {
                ListPostsScreen(navController, postViewModel, idUsuario)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text ="Bem vindo ao FEED!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF6200EE)
                    )
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF6200EE)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                TextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val retorno: Boolean = usuarioViewModel.validarLogin(email, senha)
                        if (retorno) {
                            // Liberar para o feed
                            Toast.makeText(context, "Login feito com sucesso!", Toast.LENGTH_LONG).show()
                            val idUsuario: Int = usuarioViewModel.buscarUsuarioId(email)
                            navController.navigate("CriarPost/$idUsuario")
                        } else {
                            Toast.makeText(context, "Dados Incorretos", Toast.LENGTH_LONG).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Login", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("Cadastro") },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text("Cadastro", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var textoBotao by remember { mutableStateOf("Cadastrar") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text ="Voltar para o Login",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF6200EE)
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Cadastro",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                TextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if(!usuarioViewModel.buscarUsuarioEmail(email)) {
                            usuarioViewModel.salvarUsuario(nome, email, senha)
                            Toast.makeText(context, "Salvo com sucesso!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Usuario já existe ou dados inválidos!", Toast.LENGTH_LONG).show()
                        }

                        nome = ""
                        email = ""
                        senha = ""
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = textoBotao, color = MaterialTheme.colorScheme.onPrimary)
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text("Voltar ao login", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(navController: NavController, postViewModel: PostViewModel, idUsuario: Int) {
    var conteudo by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text ="Logout",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF6200EE)
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Crie um post",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                TextField(
                    value = conteudo,
                    onValueChange = { conteudo = it },
                    label = { Text("Digite seu post aqui") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        if (conteudo.isBlank()) {
                            errorMessage = "O post não pode estar vazio!"
                            return@Button
                        } else {
                            val retorno: String = postViewModel.salvarPost(conteudo, idUsuario)
                            Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()
                        }
                        conteudo = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text("Postar", color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = { navController.navigate("ListarPost/$idUsuario") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text("Ver o feed", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPostsScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    idUsuario: Int
) {
    val listaPosts by postViewModel.listaPosts

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Posts") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("Login") {
                            popUpTo("Login") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("CriarPost/$idUsuario") }
            ) {
                Text("+")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaPosts) { post ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = post.conteudo,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}




