package sealed;

abstract sealed class Shape permits Circle, Rectangle, Square, WeirdShape { /*...*/ }

