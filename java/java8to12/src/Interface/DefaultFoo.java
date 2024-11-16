package Interface;

public class DefaultFoo implements Foo, Bar {
    String name;

    public DefaultFoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(getName());
    }

    @Override
    public String getName() {
        return this.name;
    }
}
