package com.gngsn.appxc.game.v2;

/**
 * 몬스터의 체력(health)과 공격 방식(attack)을 속성으로 포함
 */
public class Breed {
    private String name;
    private int health;
    private String attack;

    public Breed(String name, int health, String attack) {
        this.name = name;
        this.health = health;
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public String getAttack() {
        return attack;
    }
}