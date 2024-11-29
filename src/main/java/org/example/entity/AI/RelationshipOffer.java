package org.example.entity.AI;

public class RelationshipOffer{
    private int id;
    private int mainProductID;
    private int freeProductID;
    private String status;

    public RelationshipOffer() {
    }

    public RelationshipOffer(int id, int mainProductID, int freeProductID, String status) {
        this.id = id;
        this.mainProductID = mainProductID;
        this.freeProductID = freeProductID;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMainProductID() {
        return mainProductID;
    }

    public void setMainProductID(int mainProductID) {
        this.mainProductID = mainProductID;
    }

    public int getFreeProductID() {
        return freeProductID;
    }

    public void setFreeProductID(int freeProductID) {
        this.freeProductID = freeProductID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
