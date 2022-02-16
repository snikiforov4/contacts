package contacts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import contacts.client.ActivityFactory;

public class Main {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        injector.getInstance(ActivityFactory.class).createMainActivity().run();
    }
}
