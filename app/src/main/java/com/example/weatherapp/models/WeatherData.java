package com.example.weatherapp.models;

public class WeatherData {
    private Location location;
    private Current current;

    public Location getLocation() {
        return location;
    }

    public Current getCurrent() {
        return current;
    }

    public class Location {
        private String name;
        private String region;
        private String country;

        public String getName() {
            return name;
        }

        public String getRegion() {
            return region;
        }

        public String getCountry() {
            return country;
        }
    }

    public class Current {
        private double temp_c;
        private Condition condition;

        public double getTemp_c() {
            return temp_c;
        }

        public Condition getCondition() {
            return condition;
        }

        public class Condition {
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

