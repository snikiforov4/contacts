package contacts.repository;

import contacts.model.Contact;

import java.util.List;

public interface ContactsRepository {
    long count();

    List<Contact> getAll();

    Contact save(Contact contact);

    boolean removeByIndex(int index);

    Contact getByIndex(int index);
}
