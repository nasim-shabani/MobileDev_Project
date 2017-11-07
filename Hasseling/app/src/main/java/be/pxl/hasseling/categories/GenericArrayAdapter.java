package be.pxl.hasseling.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import be.pxl.hasseling.R;

import static be.pxl.hasseling.R.id.list_item_categorie_textview;

/**
 * Created by Danie on 7/11/2017.
 * Source https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 https://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass
 */

public class GenericArrayAdapter<T extends Category> extends ArrayAdapter<T> {

    private ViewHolder viewHolder;

    private static class ViewHolder {
        private TextView itemView;
        private ImageView imgV_supermarketIcon;
        private RatingBar ratingBar_stars;

    }

    public GenericArrayAdapter(Context context, ArrayList<T> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        T item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.list_item_categorie, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemView = (TextView) convertView.findViewById(list_item_categorie_textview);
            viewHolder.imgV_supermarketIcon = (ImageView) convertView.findViewById(R.id.list_item_categorie_imageview);
            viewHolder.ratingBar_stars = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item!= null) {
            final String LOG_TAG = GenericArrayAdapter.class.getSimpleName();
            // My layout has only one TextView
            // do whatever you want with your string and long
            viewHolder.itemView.setText(item.toString());
            Picasso.with(this.getContext()).load(item.formatPhotoURL()).resize(250,250).into( viewHolder.imgV_supermarketIcon);

            if(item.getRating() == null){
                viewHolder.ratingBar_stars.setVisibility(viewHolder.ratingBar_stars.INVISIBLE);

            }else{
                viewHolder.ratingBar_stars.setRating((float) roundToHalf(item.getRating().doubleValue()));
            }
        }

        return convertView;
    }

    public  double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }
}