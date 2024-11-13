package com.example.miproyectocondb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import com.example.miproyectocondb.database.AppDatabase
import com.example.miproyectocondb.database.entities.User
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializamos la base de datos
        db = AppDatabase.getDatabase(applicationContext)

        setContent {
            MiProyectoConDBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserForm(modifier = Modifier.padding(innerPadding), db = db)
                }
            }
        }
    }
}

@Composable
fun UserForm(modifier: Modifier = Modifier, db: AppDatabase) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var usersList by remember { mutableStateOf(listOf<User>()) }
    var isLoading by remember { mutableStateOf(false) }

    // Coroutine to handle DB operations
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                val newUser = User(name = name.text, email = email.text)
                coroutineScope.launch {
                    db.userDao().insert(newUser)
                    name = TextFieldValue("")
                    email = TextFieldValue("")
                }
            }) {
                Text("Insert User")
            }

            Button(onClick = {
                isLoading = true
                coroutineScope.launch {
                    usersList = db.userDao().getAllUsers()
                    isLoading = false
                }
            }) {
                Text("Show All Users")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Users: ")
            usersList.forEach { user ->
                Text("ID: ${user.id}, Name: ${user.name}, Email: ${user.email}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserFormPreview() {
    MiProyectoConDBTheme {
        UserForm(db = AppDatabase.getDatabase(LocalContext.current))
    }
}
