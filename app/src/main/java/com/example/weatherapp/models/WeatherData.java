package com.example.weatherapp.models;

import java.io.Serializable;

public class WeatherData implements Serializable {
    private Location location;
    private Current current;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public class Location implements Serializable {
        private String name;
        private String region;
        private String country;
        private String localtime;

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getCountry() {
            return country;
        }

        public String getLocaltime() {
            return localtime;
        }
    }

    public class Current implements Serializable{
        private double temp_c;
        private Condition condition;
        private int is_day;

        public double getTemp_c() {
            return temp_c;
        }

        public int getIs_day() {
            return is_day;
        }

        public Condition getCondition() {
            return condition;
        }

        public class Condition implements Serializable{
            private String text;
            private String icon;

            public String getText() {
                return text;
            }

            public String getIcon() {
                return icon;
            }
        }
    }
}

