package com.gngsn.appxb.game;

import java.awt.*;

/**
 *  Displayable 타입을 GameObject 타입의 서브타입으로 정의
 */
public interface Displayable extends GameObject {
    Point getPosition();
    void update(Graphics graphics);
}