package be.pxl.hasseling.categories;

/**
 * Created by Danie on 6/11/2017.
 */

public class Supermarket extends Category {


    public Supermarket(String placeId, String name, Long rating, Boolean openNow, String address,String photoReference) {
           super(placeId,name,rating,openNow,address,photoReference);

    }

    public Supermarket() {
        super();
    }

    @Override
    public String toString() {
        return super.getName() + "\n " +
                 formatOpen() + "\n" +
                super.getAddress();
    }
}
