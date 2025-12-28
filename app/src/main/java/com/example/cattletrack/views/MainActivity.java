package com.example.cattletrack.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.cattletrack.R;
import com.example.cattletrack.viewmodel.LoginViewModel;
import com.example.cattletrack.models.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private TextInputEditText editTextUsuario; // Este campo es para el email
    private TextInputEditText editTextContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isSessionActive()) {
            navigateToHome();
            return;
        }

        setContentView(R.layout.activity_main);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextUsuario.getText().toString();
            String password = editTextContrasena.getText().toString();
            loginViewModel.performLogin(email, password);
        });

        setupObservers();
    }

    private boolean isSessionActive() {
        SharedPreferences sharedPref = getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isLoggedIn", false);
    }

    private void setupObservers() {
        // El observador ahora recibe el objeto LoginResponse
        loginViewModel.getLoginResult().observe(this, loginResponse -> {
            Toast.makeText(this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            // Le pasamos el objeto con los datos del usuario para guardarlos
            saveSession(loginResponse);
            navigateToHome();
        });

        loginViewModel.getLoginError().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    // === El método saveSession ahora acepta un LoginResponse ===
    private void saveSession(LoginResponse response) {
        SharedPreferences sharedPref = getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", true);
        // Guardamos los datos adicionales que nos dio la API
        editor.putInt("userId", response.getIdUsuario());
        editor.putInt("userType", response.getTipoUsuario());
        editor.apply(); // Aplicamos los cambios
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, BottomNavegationView.class);
        startActivity(intent);
        finish();
    }
}