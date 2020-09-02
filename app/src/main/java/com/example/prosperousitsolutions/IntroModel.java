package com.example.prosperousitsolutions;

public class IntroModel {
    String heading, subheading, backgroundColor;

    public IntroModel(String heading, String subheading, String backgroundColor) {
        this.heading = heading;
        this.subheading = subheading;
        this.backgroundColor = backgroundColor;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
