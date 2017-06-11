package com.un.testdemo;

import java.util.List;

/**
 * introduce:
 * author : mrli
 * date: 6/8/17.
 */

public class TrackPointBean {

    /**
     * vin : P011000700000045
     * coordinates : [{"longitude":121.893565,"latitude":29.749897},{"longitude":121.893565,"latitude":29.749897},{"longitude":121.893565,"latitude":29.749897}]
     */

    private String vin;
    private List<CoordinatesBean> coordinates;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<CoordinatesBean> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<CoordinatesBean> coordinates) {
        this.coordinates = coordinates;
    }


    public static class CoordinatesBean {
        /**
         * longitude : 121.893565
         * latitude : 29.749897
         */

        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
