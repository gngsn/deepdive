package com.gngsn.appxc.game;

import com.gngsn.appxc.game.v1.breed.Dragon;
import com.gngsn.appxc.game.v1.breed.Troll;
import com.gngsn.appxc.game.v2.Breed;
import com.gngsn.appxc.game.v2.Monster;

public class Client {
    public static void main(String[] args) {
        /**
         * v1: 새로운 몬스터의 종류를 추가하는 것은 새로운 클래스를 추가
         */
        com.gngsn.appxc.game.v1.Monster dragon_v1 = new Dragon();
        com.gngsn.appxc.game.v1.Monster troll_v1 = new Troll();

        /**
         * v2: 새로운 Breed 인스턴스를 생성하고 Monster 인스턴스에 연결
         */
        com.gngsn.appxc.game.v2.Monster dragon = new Monster(new Breed("용", 230, "용은 불을 내뿜는다"));
        com.gngsn.appxc.game.v2.Monster troll = new Monster(new Breed("트롤", 48, "트롤은 곤봉으로 때린다"));
    }
}
