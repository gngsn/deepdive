package com.gngsn.appxc.game.v1.breed;

import com.gngsn.appxc.game.v1.Monster;

public class Troll extends Monster {

    public Troll() {
        super(48);
    }

    @Override
    public String getAttack() {
        return "트롤은 곤봉으로 때린다";
    }
}