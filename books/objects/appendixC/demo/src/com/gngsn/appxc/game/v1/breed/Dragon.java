package com.gngsn.appxc.game.v1.breed;

import com.gngsn.appxc.game.v1.Monster;

public class Dragon extends Monster {

    public Dragon() {
        super(230);
    }

    @Override
    public String getAttack() {
        return "용은 불을 내뿜는다";
    }
}
