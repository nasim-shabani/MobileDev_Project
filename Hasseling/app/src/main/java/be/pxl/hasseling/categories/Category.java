package be.pxl.hasseling.categories;

import be.pxl.hasseling.BuildConfig;

/**
 * Created by Danie on 7/11/2017.
 */

public abstract class Category {
    private String name;
    private String placeId;
    private Long rating;
    private Boolean openNow;
    private String address;
    private String photoReference;

    public Category(String placeId,String name, Long rating, Boolean openNow, String address,String photoReference) {
        this.placeId = placeId;
        this.name = name;
        this.rating = rating;
        this.openNow = openNow;
        this.address = address;
        this.photoReference = photoReference;
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

    String formatOpen(){
        if(openNow != null){
            return ((openNow == true) ? "OPEN NOW!" : "CLOSED ATM");
        }else{
            return "Opening Unknown";
        }
    }
}
