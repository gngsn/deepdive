package recordPatterns;

record GPS(double latitude, double longitude) {}
record Location (String name, GPS gps) {}

public class InstanceOfExample {
    public static void main(String[] args) {
        Location location = new Location("Home", new GPS(1.0, 2.0));

        if (location instanceof Location(String name GPS(var latitude, var longitude))) {
            System.out.printf("%s is located at (%s,%s)",
                    name, latitude, longitude);
        }
    }
}
