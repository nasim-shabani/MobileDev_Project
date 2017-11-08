package be.pxl.hasseling.categories;

import be.pxl.hasseling.BuildConfig;

/**
 * Created by Danie on 7/11/2017.
 */

public abstract class Category {
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

    public String formatPhotoURL(){
        if(photoReference.equals("default")){
            return "https://png.icons8.com/ingredients/color/50/000000";
        }
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + BuildConfig.GOOGLE_PLACES_API_KEY;
    }

    String formatRating(){
        if(rating != null){
           return Math.round(rating) + " stars";
        }else{
            return  "No Starrating";
        }

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

}
