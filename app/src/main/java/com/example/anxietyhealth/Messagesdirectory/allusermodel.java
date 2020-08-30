package com.example.anxietyhealth.Messagesdirectory;

public class allusermodel {

    private String name;
    private String type;
    private String profileimage;

    public allusermodel(String name, String type, String profileimage) {
        this.name = name;
        this.type = type;
        this.profileimage = profileimage;
    }

    public allusermodel() {
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String namen) {
        name = namen;
    }

    public String getInstitute() {
        return type;
    }

    public void setInstitute(String institute) {
        type = institute;
    }
}
