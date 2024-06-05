package com.example.gamewiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gamewiki.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private List<String> searchData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Добавление данных для поиска (это просто пример, вы должны добавить свои фактические данные)
        searchData.add("HomeActivity");
        searchData.add("GameActivity");
        searchData.add("SectionsActivity");
        searchData.add("DetailsActivity");

        Button start = findViewById(R.id.starting);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_game_s, R.id.nav_sections, R.id.nav_details)
                .setOpenableLayout(drawer)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Обработка поискового запроса
                handleSearchQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Обработка изменения текста для предложений поиска в реальном времени (по желанию)
                return false;
            }
        });

        return true;
    }

    private void handleSearchQuery(String query) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Получаем все разделы из базы данных
        List<String> allSections = dbHelper.getAllSections();

        // Получаем все элементы из базы данных
        List<String> allItems = dbHelper.getAllItems();

        // Соединяем все данные в один список для поиска
        List<String> allData = new ArrayList<>();
        allData.addAll(allSections);
        allData.addAll(allItems);

        // Осуществляем поиск
        List<String> result = new ArrayList<>();
        for (String item : allData) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                result.add(item);
            }
        }

        // Обрабатываем результаты поиска
        if (result.isEmpty()) {
            Toast.makeText(this, "Нет результатов", Toast.LENGTH_SHORT).show();
        } else {
            for (String item : result) {
                Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            }
            // Передаем имя элемента в Activity для отображения деталей
            navigateToItemDetails(result.get(0));
        }
    }

    private void navigateToItemDetails(String itemName) {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("itemName", itemName);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
