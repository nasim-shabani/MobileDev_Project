package be.pxl.hasseling.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import be.pxl.hasseling.Fragment.CategoryFragment;
import be.pxl.hasseling.Fragment.DirectionFragment;
import be.pxl.hasseling.Fragment.HomeFragment;
import be.pxl.hasseling.Fragment.SOSFragment;
import be.pxl.hasseling.R;

public class MainActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout nDrawerLayout;
    private ActionBarDrawerToggle nToggle;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;
    private static final String TAG_SUPERMARKET = "supermarket";
    private static final String TAG_RESTAURANT = "restaurant";
    private static final String TAG_LAUNDRY = "laundry";
    private static final String TAG_DRINK = "drink";
    private static final String TAG_DIRECTION = "direction";
    private static final String TAG_CLUB = "club";
    private static final String TAG_FITNESS = "fitness";
    private static final String TAG_SOS = "SOS";

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        nDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        nToggle = new ActionBarDrawerToggle(this, nDrawerLayout, R.string.open, R.string.close);

        nDrawerLayout.addDrawerListener(nToggle);
        nToggle.syncState();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (nToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private Fragment getHomeFragment() {
        Fragment categoryFragment = new CategoryFragment();//DANIE
        Bundle bundle = new Bundle();//DANIE
        bundle.clear(); //DANIE

        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // supermarket
               /* SupermarketFragment supermarketFragment = new SupermarketFragment();
                return supermarketFragment;*/
                bundle.putString("Keyword","convenience_store");//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                return categoryFragment;//DANIE
            case 2:
                // restaurant fragment
               /* RestaurantFragment restaurantFragment = new RestaurantFragment();
                return restaurantFragment;*/
                bundle.putString("Keyword","restaurants");//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                return categoryFragment;//DANIE
            case 3:
                // laundry fragment
                /*LaundryFragment laundryFragment = new LaundryFragment();
                return laundryFragment;*/
                bundle.putString("Keyword","laundry");//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                return categoryFragment;//DANIE
            case 4:
                // drink fragment
                /*DrinkFragment drinkFragment = new DrinkFragment();
                return drinkFragment;*/
                bundle.putString("Keyword","drink");//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                return categoryFragment;//DANIE
            case 5:
                // transport fragment
                DirectionFragment directionFragment = new DirectionFragment();
                return directionFragment;
            case 6:
                // club fragment
               /* ClubFragment clubFragment = new ClubFragment();
                return clubFragment;*/
                bundle.putString("Keyword","club");//DANIE
                categoryFragment.setArguments(bundle);//DANIE
                return categoryFragment;//DANIE
             case 7:
                // fitness fragment
                /*FitnessFragment fitnessFragment = new FitnessFragment();
                return fitnessFragment;*/
                 bundle.putString("Keyword","fitness");//DANIE
                 categoryFragment.setArguments(bundle);//DANIE
                 return categoryFragment;//DANIE
             case 8:
                // to do fragment
                SOSFragment SOSFragment = new SOSFragment();
                return SOSFragment;

            default:
                return new HomeFragment();
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            nDrawerLayout.closeDrawers();
            return;
        }

        // when switching between navigation menus
        // the fragment is loaded with cross fade effect

        // update the main content by replacing fragments
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.content_frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

        //Closing drawer on item click
        nDrawerLayout.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (nDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            nDrawerLayout.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                // Create a new fragment and specify the fragment to show based on nav item clicked
                Fragment fragment = null;
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;

                       /* HomeFragment homeFragment = new HomeFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.content_frame, homeFragment)
                                .commit();
                                */
                        break;
                    case R.id.nav_supermarket:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SUPERMARKET;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_restaurant:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_RESTAURANT;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_laundry:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LAUNDRY;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_drink:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_DRINK;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_direction:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_DIRECTION;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_club:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_CLUB;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_fitness:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_FITNESS;
                        nDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_sos:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_SOS;
                        nDrawerLayout.closeDrawers();
                        break;
                    /*case R.id.nav_restaurant:
                        navItemIndex = 1;
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, RestaurantActivity.class));
                        nDrawerLayout.closeDrawers();
                        break;*/
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                }


                // Insert the fragment by replacing any existing fragment
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }


}