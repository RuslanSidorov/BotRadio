package com.example.botradio;

 class Comments
{
}

 class Song
{

    private String album;

    private String artist;

    private Comments comments;

    private int timestamp;

    private String title;

    private String cover;

    private String year;

    public void setAlbum(String album){
        this.album = album;
    }
    public String getAlbum(){
        return this.album;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }
    public String getArtist(){
        return this.artist;
    }
    public void setComments(Comments comments){
        this.comments = comments;
    }
    public Comments getComments(){
        return this.comments;
    }
    public void setTimestamp(int timestamp){
        this.timestamp = timestamp;
    }
    public int getTimestamp(){
        return this.timestamp;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setCover(String cover){
        this.cover = cover;
    }
    public String getCover(){
        return this.cover;
    }
    public void setYear(String year){
        this.year = year;
    }
    public String getYear(){
        return this.year;
    }
}




