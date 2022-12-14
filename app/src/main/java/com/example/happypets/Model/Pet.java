package com.example.happypets.Model;

public class Pet {

    private String id;
    private String name;
    private String breed;
    private String gender;
    private String age;
    private String category;
    private String imageURL;
    private String ownerId;

    public Pet(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Pet(String name, String breed, String gender, String category, String imageURL, String id, String ownerId, String age) {
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.category = category;
        this.imageURL = imageURL;
        this.id = id;
        this.ownerId = ownerId;
        this.age = age;
    }



}
