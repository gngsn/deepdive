package sealed;
public sealed class Figure
        // The permits clause has been omitted
        // as its permitted classes have been
        // defined in the same file.
{ }

final class CircleFigure extends Figure {
    float radius;
}
non-sealed class SquareFigure extends Figure {
    float side;
}
sealed class RectangleFigure extends Figure {
    float length, width;
}
final class FilledRectangleFigure extends RectangleFigure {
    int red, green, blue;
}
