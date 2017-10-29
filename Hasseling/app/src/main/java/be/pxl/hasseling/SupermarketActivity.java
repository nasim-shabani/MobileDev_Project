package be.pxl.hasseling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SupermarketActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);

/*
        TextView textView = (TextView) findViewById(
                R.id.list_item_supermarket_textview);
        textView.setText("blub SupermarketActivity blub");

       listView = (ListView)findViewById(R.id.ListViewSupermarket);

        String [] arraySupermarkets = {
                "ALDI - 2km - 20/10", "Spar - 4km - 20/10",
               "LIDL - 1km - 70/00", "Delhaize - 7km - 20/10",
                "Carrefour- 4km - 20/10", "Biologic - 500m - 20/10"};

                List<String> supermarkets = new ArrayList<String>(
                Arrays.asList(arraySupermarkets));

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, // context of the activity
                R.layout.list_item_categorie,// type of list view
                supermarkets//array as a third parameter
        );

        listView.setAdapter(arrayAdapter);
*/


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
}
