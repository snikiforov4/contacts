package contacts.client;

import contacts.client.activity.Activity;
import contacts.model.Contact;

import javax.inject.Named;

public interface ActivityFactory {
    @Named("main")
    Activity createMainActivity();

    @Named("list")
    Activity createListActivity();

    @Named("record")
    Activity createRecordActivity(Contact contact);
}
