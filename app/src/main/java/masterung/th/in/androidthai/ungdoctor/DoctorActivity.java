package masterung.th.in.androidthai.ungdoctor;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

public class DoctorActivity extends AppCompatActivity {

    private String idString;
    private String urlPHP = "https://www.androidthai.in.th/sam/getUserWhereIdMaster.php";
    private String nameString, surnameString;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        getUser();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentDoctorFragment, new AboutMeFragment()).commit();
        }

    }   // Main Method

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void getUser() {
        idString = getIntent().getStringExtra("id");
        Log.d("26JanV1", "id Receive ==> " + idString);

        try {

            GetUserWhereIdThread getUserWhereIdThread = new GetUserWhereIdThread(DoctorActivity.this);
            getUserWhereIdThread.execute(idString, urlPHP);
            String json = getUserWhereIdThread.get();
            Log.d("26JanV1", "json Doctor ==> " + json);

            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            nameString = jsonObject.getString("Name");
            surnameString = jsonObject.getString("Surname");

            createToolbar();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbarDoctor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setSubtitle(nameString + " " + surnameString);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_hamberker);

        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DoctorActivity.this, drawerLayout, R.string.open, R.string.close);

    }

}   // Main Class
