package edu.brown.cs.student.main;

public class StarData {
    private String starID;
    private String name;
    private double x;
    private double y;
    private double z;

    /**
     *
     * @param starID - star ID
     * @param name - name of star
     * @param x - x coordinate
     * @param y - y coordinate
     * @param z - z coordinate
     */
    public StarData(String starID, String name, double x, double y, double z) {
        this.starID = starID;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return - starID
     */
    public String getStarID() {
        return starID;
    }
    /**
     * @return - name
     */
    public String getName() {
        return name;
    }
    /**
     * @return - x coordinate
     */
    public double getX() {
        return x;
    }
    /**
     * @return - y coordinate
     */
    public double getY() {
        return y;
    }
    /**
     * @return - z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * This method returns a stating whether the two objects are equivalent
     * @param star - A StarData object to be checked for equality
     * @return - boolean
     */
    public boolean equals(StarData star) {
        if (!this.getStarID().equals(star.getStarID())) return false;
        if (!this.getName().equals(star.getName())) return false;
        if (this.getX() != star.getX()) return false;
        if (this.getY() != star.getY()) return false;
        if (this.getZ() != star.getZ()) return false;
        return true;
    }

}
