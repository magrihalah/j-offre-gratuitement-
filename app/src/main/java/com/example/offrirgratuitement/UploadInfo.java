package com.example.offrirgratuitement;

public class UploadInfo {
    public String imageName;
    public String imageURL;
    public String imageBio;
    public String imageNum;
    public String imageVille;
    public UploadInfo(){}

    public UploadInfo(String name, String bio , String num , String ville , String url) {
        this.imageName = name;
        this.imageURL = url;
        this.imageBio = bio ;
        this.imageNum = num ;
        this.imageVille = ville ;
    }

    public String getImageVille() {
        return imageVille;
    }

    public String getImageBio() {
        return imageBio;
    }

    public String getImageNum() {
        return imageNum;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setImageBio(String imageBio) {
        this.imageBio = imageBio;
    }

    public void setImageNum(String imageNum) {
        this.imageNum = imageNum;
    }

    public void setImageVille(String imageVille) {
        this.imageVille = imageVille;
    }
}
