package com.gngsn.appxc.game.v2;

public class Monster {
    private int health;
    private Breed breed;

    public Monster(Breed breed) {
        this.health = breed.getHealth();
    }

    public String getAttack() {
        return breed.getAttack();
    }
}
