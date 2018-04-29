package com.limingjian.liteplayer.bean;

public class FileBean {

    String path;
    String type;
    String name;
    int numMedia;
    int numSonFolder;
    boolean isDir;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumMedia() {
        return numMedia;
    }

    public void setNumMedia(int numMedia) {
        this.numMedia = numMedia;
    }

    public int getNumSonFolder() {
        return numSonFolder;
    }

    public void setNumSonFolder(int numSonFolder) {
        this.numSonFolder = numSonFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }
}
