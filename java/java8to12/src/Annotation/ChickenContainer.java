package Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// 감쌀 Annotation 의 설정보다 범위가 같거나 넓어야 한다.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface ChickenContainer {
    // 감쌀 Annotation을 배열로 갖고 있어야 함
    Chicken[] value();

}
