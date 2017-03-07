package com.example.mk.mysmartsns.model;

/**
 * Created by mk on 2017-02-22.
 */

public class Interest_item {
    private String interest;
    private boolean clicked;

    public Interest_item(String bigHashName, boolean clicked){
        this.interest = bigHashName;
        this.clicked = clicked;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
