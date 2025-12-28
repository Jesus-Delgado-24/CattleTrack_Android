package com.example.cattletrack.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.cattletrack.R;
import com.example.cattletrack.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BottomNavegationView extends AppCompatActivity implements MonitoreoFragment.OnFragmentInteractionListener, FragmentHistorialReproduccion.OnFragmentInteractionH {

    // Fragmentos
    HomeFragment homeFragment = new HomeFragment();
    AlmacenFragment almacenFragment = new AlmacenFragment();
    ProduccionFragment produccionFragment = new ProduccionFragment();
    MonitoreoFragment monitoreoFragment = new MonitoreoFragment();
    FragmentHistorialReproduccion historialReproduccionFragment = new FragmentHistorialReproduccion();
    Abastecer abastecerFragment = new Abastecer();

    // Vistas
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navegation_view);

        // 1. Inicialización de Vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. Configuración del Toolbar y Drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Cargar fragmento inicial
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.homeFragment);
        }

        // 4. Configurar Listeners de Navegación (Tu código original)
        setupNavigationListeners();

        // 5. INICIALIZAR VIEWMODEL Y CARGAR DATOS DEL USUARIO
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        setupNavHeader();  // Configura el header inicial
        setupObservers();  // Observa cuando lleguen los datos de la API
    }

    // Configura el header con los datos locales y pide los remotos
    private void setupNavHeader() {
        // Obtener referencia al header del menú lateral
        View headerView = navigationView.getHeaderView(0);
        TextView lblIdUsuario = headerView.findViewById(R.id.lblid_U);
        TextView lblNombreUsuario = headerView.findViewById(R.id.lblname_U);
        TextView lblRolUsuario = headerView.findViewById(R.id.lblrol_U);

        // Leer datos de sesión
        SharedPreferences sharedPref = getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", 0);
        int userType = sharedPref.getInt("userType", 0);

        // Mostrar datos inmediatos (ID y Rol)
        lblIdUsuario.setText("ID: " + userId);
        lblRolUsuario.setText("Rol: " + getRolAsString(userType));

        // Intentar cargar nombre (primero de caché, luego de API)
        String cachedName = sharedPref.getString("userName", null);
        if (cachedName != null) {
            lblNombreUsuario.setText(cachedName);
        } else {
            lblNombreUsuario.setText("Cargando...");
        }

        // Si tenemos un ID válido, pedimos los datos frescos a la API
        if (userId != 0) {
            userViewModel.fetchUserDetails(userId);
        }
    }

    // Observa la respuesta de la API
    private void setupObservers() {
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                // Actualizar el nombre en el header
                View headerView = navigationView.getHeaderView(0);
                TextView lblNombreUsuario = headerView.findViewById(R.id.lblname_U);

                String nombreCompleto = user.getNombreCompleto();
                lblNombreUsuario.setText(nombreCompleto);

                // Guardar en caché para la próxima vez
                SharedPreferences sharedPref = getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
                sharedPref.edit().putString("userName", nombreCompleto).apply();
            }
        });

        // Manejar errores silenciosamente o con log
        userViewModel.getError().observe(this, error -> {
            // No mostramos Toast para no molestar al usuario si falla la carga del nombre en segundo plano
            System.out.println("Error cargando usuario: " + error);
        });
    }

    // Convierte el número de rol a texto
    private String getRolAsString(int userType) {
        switch (userType) {
            case 1: return "Dueño";
            case 2: return "Admin";
            case 3: return "Trabajador";
            case 4: return "Veterinario";
            default: return "Desconocido";
        }
    }

    private void setupNavigationListeners() {
        // Listener del BottomNavigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.homeFragment) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.almacenFragment) {
                loadFragment(almacenFragment);
                return true;
            } else if (itemId == R.id.produccionFragment) {
                loadFragment(produccionFragment);
                return true;
            } else if (itemId == R.id.monitoreoFragment) {
                loadFragment(monitoreoFragment);
                return true;
            } else if (itemId == R.id.historialFragment) {
                loadFragment(historialReproduccionFragment);
                return true;
            } else if (itemId == R.id.abastecerFragment) {
                loadFragment(abastecerFragment);
                return true;
            }
            return false;
        });

        // Listener del NavigationDrawer (Menú Lateral)
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.homeFragment) {
                loadFragment(homeFragment);
            } else if (id == R.id.almacenFragment) {
                loadFragment(almacenFragment);
            } else if (id == R.id.produccionFragment) {
                loadFragment(produccionFragment);
            } else if (id == R.id.monitoreoFragment) {
                loadFragment(monitoreoFragment);
            } else if (id == R.id.historialFragment) {
                loadFragment(historialReproduccionFragment);
            } else if (id == R.id.nav_logout) {
                logout();
            } else if (id == R.id.abastecerFragment) {
                loadFragment(abastecerFragment);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void loadFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_right, R.anim.slide_out_left,
                    R.anim.slide_in_left, R.anim.slide_out_right
            );
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error cargando fragment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadFragment(Fragment fragment) {
        loadFragment(fragment);
    }

    private void logout() {
        Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPref = getSharedPreferences("CattleTrackPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userId");
        editor.remove("userType");
        editor.remove("userName");
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}