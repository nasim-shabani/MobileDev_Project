package be.pxl.hasseling.categories;

import be.pxl.hasseling.BuildConfig;
import be.pxl.hasseling.R;

/**
 * Created by Danie on 7/11/2017.
 */

public class Category {
    private String address;
    String formatted_phone_number;
    String international_phone_number;
    private String name;
    private String placeId;
    private Long rating;
    private Boolean openNow;
    String weekday_text;
    private String photoReference;
    String url, website;
    String KEYWORD_TAG;

    public Category(String placeId,String name, Long rating, Boolean openNow, String address,String photoReference) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.openNow = openNow;
        this.address = address;
        this.photoReference = photoReference;
    }

    public Category() {

    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Boolean getOpenNow() {
        return openNow;
    }
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getKEYWORD_TAG() {
        return KEYWORD_TAG;
    }

    public void setKEYWORD_TAG(String KEYWORD_TAG) {
        this.KEYWORD_TAG = KEYWORD_TAG;
    }

    public String formatPhotoURL(){
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY;
    }

    public int getPhotoDefault() {
        return R.drawable.header_hasseling;
    }
    
    public static int getDefaultIcon(String KEYWORD_TAG){
        int defaultPhoto = R.mipmap.ic_launcher;
        switch (KEYWORD_TAG) {
            case "convenience_store":
              //  defaultPhoto = "https://png.icons8.com/ingredients/color/50/000000";
                defaultPhoto= R.mipmap.ic_supermarket;
                break;
            case "restaurant":
            //    defaultPhoto = "https://png.icons8.com/?id=12162&size=2x";
                defaultPhoto= R.mipmap.ic_restaurant;
                break;
            case "laundry":
              //  defaultPhoto = "https://png.icons8.com/?id=12834&size=560";
                defaultPhoto= R.mipmap.ic_laundry;
                break;
            case "cafe":
              //  defaultPhoto = "https://png.icons8.com/?id=13300&size=2x";
                defaultPhoto= R.mipmap.ic_drink;
                break;
            case "club":
               // defaultPhoto = "https://png.icons8.com/?id=16883&size=2x";
                defaultPhoto= R.mipmap.ic_club;
                break;
            case "fitness":
              //  defaultPhoto = "https://png.icons8.com/?id=12975&size=560";
                defaultPhoto= R.mipmap.ic_fitness;
                break;
        }
    return defaultPhoto;
    }

    public String formatOpen(){
        if(openNow != null){
            return ((openNow == true) ? "OPEN NOW!" : "CLOSED ATM");
        }else{
            return "Opening Unknown";
        }
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public String getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(String weekday_text) {
        this.weekday_text = weekday_text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return getName() + "\n " +
                formatOpen() + "\n" +
                getAddress();
    }
}
