package Pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.pocketcontrol.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        BottomNavigationView botNav = findViewById(R.id.bottomNavigationView);
        botNav.setOnItemSelectedListener(navListener);
        botNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        botNav.setItemHorizontalTranslationEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragment, new Overview()).commit();
    }

    private final BottomNavigationView.
            OnItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.overview:
                selectedFragment = new Overview();
                this.setTitle("Overview");
                break;
            case R.id.spendings:
                selectedFragment = new Spendings();
                this.setTitle("Spendings");
                break;
            case R.id.budgeting:
                selectedFragment = new Budgeting();
                this.setTitle("Budgeting");
                break;
            case R.id.settings:
                selectedFragment = new Settings();
                this.setTitle("Settings");
                break;

        }
        // It will help to replace the
        // one fragment to other.
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim) // set animation between page transition
                .replace(R.id.homeFragment, selectedFragment)
                .commit();
        return true;
    };
}