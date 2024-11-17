package com.gngsn.appxb.game;

/**
 * 화면에 표시되지 않더라도 게임에 다양한 효과를 부여할 수 있는 부가적인 요소
 */
public interface Effect extends GameObject {
    void activate();
}