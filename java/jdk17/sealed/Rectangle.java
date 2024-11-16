package sealed;

sealed class Rectangle extends Shape permits TransparentRectangle, FilledRectangle { /*...*/
}
