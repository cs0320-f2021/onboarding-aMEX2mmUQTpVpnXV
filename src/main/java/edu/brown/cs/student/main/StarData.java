package edu.brown.cs.student.main;

public class StarData {
    private String starID;
    private String name;
    private double x;
    private double y;
    private double z;

    public StarData(String starID, String name, double x, double y, double z) {
        this.starID = starID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getStarID() {
        return starID;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }


    public boolean equals(StarData star) {
        if (!this.getStarID().equals(star.getStarID())) return false;
        if (!this.getName().equals(star.getName())) return false;
        if (this.getX() != star.getX()) return false;
        if (this.getY() != star.getY()) return false;
        if (this.getZ() != star.getZ()) return false;
        return true;
    }

}
