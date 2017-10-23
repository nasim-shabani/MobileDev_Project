package be.pxl.hasseling;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btn_supermarket, btn_restaurant, btn_laundry,btn_drink,btn_transport,btn_club,btn_fitness,btn_todo;

    //Niet wegdoen aub nasnas
    //dit dient om via de mainpage naar alle categorieen te gaan in een aparte scherm
    public void catogeries() {
        btn_supermarket = (Button) findViewById(R.id.btn_supermarket);
        btn_restaurant = (Button) findViewById(R.id.btn_restaurant);
        btn_laundry = (Button) findViewById(R.id.btn_laundry);
        btn_drink = (Button) findViewById(R.id.btn_drink);
        btn_transport = (Button) findViewById(R.id.btn_transport);
        btn_club = (Button) findViewById(R.id.btn_club);
        btn_fitness = (Button) findViewById(R.id.btn_fitness);
        btn_todo = (Button) findViewById(R.id.btn_todo);

      btn_supermarket.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent supermarket = new Intent(MainActivity.this, SupermarketActivity.class);
                                                   startActivity(supermarket);
                                               }
                                           }
        );
        btn_restaurant.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent restaurant = new Intent(MainActivity.this, RestaurantActivity.class);
                                                   startActivity(restaurant);
                                               }
                                           }
        );

        btn_laundry.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent laundry = new Intent(MainActivity.this, LaundryActivity.class);
                                                   startActivity(laundry);
                                               }
                                           }
        );

        btn_drink.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent drink = new Intent(MainActivity.this, DrinkActivity.class);
                                                   startActivity(drink);
                                               }
                                           }
        );

        btn_transport.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent transport = new Intent(MainActivity.this, TransportActivity.class);
                                                   startActivity(transport);
                                               }
                                           }
        );

        btn_club.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent club = new Intent(MainActivity.this, ClubActivity.class);
                                                   startActivity(club);
                                               }
                                           }
        );
        btn_fitness.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent fitness = new Intent(MainActivity.this, FitnessActivity.class);
                                                   startActivity(fitness);
                                               }
                                           }
        );

        btn_todo.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent todo = new Intent(MainActivity.this, TodoActivity.class);
                                                   startActivity(todo);
                                               }
                                           }
        );


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catogeries();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Als je deze Code in commentaar niet nodig heb doe ze weg.
        //Beetje onoverzichtelijk :(

//        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .add(R.id.content_frame, new ---())
//                    .commit();
//        }
//
//
//        // Locate the button in activity_main.xml
//            btn_supermarket = (Button) findViewById(R.id.btn_supermarket);
//            btn_restaurant = (Button) findViewById(R.id.btn_restaurant);
//            btn_laundry = (Button) findViewById(R.id.btn_laundry);
//
//            // Capture button clicks
//            btn_supermarket.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//
//                FragmentManager fragmentManager = getFragmentManager();
//                // Start NewActivity
//                fragmentManager.beginTransaction()
//                        .replace(R.id.content_frame, new SupermarketFragment())
//                        .commit();
//            }
//
//        });


        //ViewPager view = (ViewPager) findViewById(R.id.drawer_layout);
        // fragmentManager = getFragmentManager();
        //FragmentManager fragment = getFragmentManager();
        //fragment = getItem(0);
        //view.setAdapter(fragment);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


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
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_supermarket_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new SupermarketFragment())
                    .commit();
        } else if (id == R.id.nav_restaurant_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new RestaurantFragment())
                    .commit();

        } else if (id == R.id.nav_laundry_layout) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new LaundryFragment())
                    .commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.restaurant_layout,
                container, false);
        Button button = (Button) view.findViewById(R.id.btn_supermarket);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new SupermarketFragment())
                        .commit();
            }
        });
        return view;
    }

}
