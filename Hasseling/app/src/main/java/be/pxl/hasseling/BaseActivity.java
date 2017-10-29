package be.pxl.hasseling;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Nasnas - dit doet niet wat het moet....
    // het laat enkel de tekst in het midden van de scherm zien ipv aparte venster zoals mainpage
    //DaDan-----------> Dit werkt nu wel. Ik heb die activities gestart ipv die fragments. ;)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_supermarket) {
            startActivity(new Intent(this, SupermarketActivity.class));
        } else if (id == R.id.nav_restaurant) {
            Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_laundry) {
            Intent intent = new Intent(getApplicationContext(), LaundryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_drink) {
            startActivity(new Intent(this, DrinkActivity.class));
        } else if (id == R.id.nav_transport) {
            startActivity(new Intent(this, TransportActivity.class));
        } else if (id == R.id.nav_club) {
            startActivity(new Intent(this, ClubActivity.class));
        } else if (id == R.id.nav_fitness) {
            startActivity(new Intent(this, FitnessActivity.class));
        } else if (id == R.id.nav_todo) {
            startActivity(new Intent(this, TodoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
