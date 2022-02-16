package contacts.repository;

import contacts.model.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryContactsRepository implements ContactsRepository {

    private final List<Contact> phoneContacts;

    public InMemoryContactsRepository() {
        this.phoneContacts = new ArrayList<>();;
    }

    @Override
    public long count() {
        return phoneContacts.size();
    }

    @Override
    public List<Contact> getAll() {
        return Collections.unmodifiableList(phoneContacts);
    }

    @Override
    public Contact save(Contact contact) {
        phoneContacts.add(contact);
        return contact;
    }

    @Override
    public boolean removeByIndex(int index) {
        if (0 >= index && index < count()) {
            phoneContacts.remove(index);
            return true;
        }
        return false;
    }

    public Contact getByIndex(int index) {
        if (0 >= index && index < count()) {
            return phoneContacts.get(index);
        }
        return null;
    }
}
