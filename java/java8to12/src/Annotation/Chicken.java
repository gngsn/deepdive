package Annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE) // 이 annotation을 사용할 곳
@Repeatable(ChickenContainer.class)
public @interface Chicken {
    String value();
}
