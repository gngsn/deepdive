package factory;

import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Movie;

public class Client {
    private Factory factory;

    public Client(Factory factory) {

        this.factory = factory;

    }

    public Money getAvatarFee() {
        Movie avatar = factory.createAvatarMovie();
        return avatar.getFee();
    }
}