package com.myself.todo.Beans;

public class Music {
    private int id;



    private String music;
    private String artist;
    private String album;
    private String description;
    private String dia;
    private String musicuri;
    private String status;

    public Music(String music, String artist, String album, String description, String dia, String musicuri) {
        this.music = music;
        this.artist = artist;
        this.album = album;
        this.description = description;
        this.dia = dia;
        this.musicuri = musicuri;
    }

    public Music() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMusicuri() {
        return musicuri;
    }

    public void setMusicuri(String musicuri) {
        this.musicuri = musicuri;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}