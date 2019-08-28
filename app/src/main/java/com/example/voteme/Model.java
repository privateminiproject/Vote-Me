package com.example.voteme;

public class Model {

    public String id,Name,Discription,Image;

    public Model(String name, String discription, String image) {
        Name = name;
        Discription = discription;
        Image = image;
    }

    public Model(String name, String discription) {
        Name = name;
        Discription = discription;
    }

    public Model(String id) {
        this.id = id;
    }

    public Model(String id, String name, String discription, String image) {
        this.id = id;
        Name = name;
        Discription = discription;
        Image = image;
    }

    public Model() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
