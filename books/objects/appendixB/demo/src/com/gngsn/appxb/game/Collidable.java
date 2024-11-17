package com.gngsn.appxb.game;

/**
 * 다른 요소들과의 충돌로 인해 이동에 제약을 받거나 피해를 입는 등의 처리가 필요한 객체를 위한 Collidable 타입 정의
 * - 충돌 체크를 위한 collideWith 오퍼레이션을 추가
 */
public interface Collidable extends Displayable {
    boolean collideWith(Collidable other);

}