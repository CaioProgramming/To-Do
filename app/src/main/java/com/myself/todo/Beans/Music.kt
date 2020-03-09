package com.myself.todo.Beans

import org.junit.runner.RunWith

class Music {
    private var id = 0
    private var music: String? = null
    private var artist: String? = null
    private var album: String? = null
    private var description: String? = null
    private var dia: String? = null
    private var musicuri: String? = null
    private var status: String? = null

    constructor(music: String?, artist: String?, album: String?, description: String?, dia: String?, musicuri: String?) {
        this.music = music
        this.artist = artist
        this.album = album
        this.description = description
        this.dia = dia
        this.musicuri = musicuri
    }

    constructor() {}

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getMusicuri(): String? {
        return musicuri
    }

    fun setMusicuri(musicuri: String?) {
        this.musicuri = musicuri
    }

    fun getMusic(): String? {
        return music
    }

    fun setMusic(music: String?) {
        this.music = music
    }

    fun getArtist(): String? {
        return artist
    }

    fun setArtist(artist: String?) {
        this.artist = artist
    }

    fun getAlbum(): String? {
        return album
    }

    fun setAlbum(album: String?) {
        this.album = album
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getDia(): String? {
        return dia
    }

    fun setDia(dia: String?) {
        this.dia = dia
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }
}