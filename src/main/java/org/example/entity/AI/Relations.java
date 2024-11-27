package org.example.entity.AI;

public class Relations {
    private Item dependents;
    private Item item2;
    private boolean active;

    public Item getDependents() {
        return dependents;
    }

    public void setDependents(Item dependents) {
        this.dependents = dependents;
    }

    public Item getItem2() {
        return item2;
    }

    public void setItem2(Item item2) {
        this.item2 = item2;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
