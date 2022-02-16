package contacts.controller;

import contacts.controller.dto.ContactDto;
import contacts.model.Contact;
import contacts.model.response.Response;
import contacts.service.ContactsService;
import lombok.AllArgsConstructor;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
public class ContactsController {

    public static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(ZoneOffset.UTC);

    private final ContactsService contactsService;

    public long count() {
        return contactsService.count();
    }

    public List<? extends Contact> getAllContacts() {
        return contactsService.getAllContacts();
    }

    public List<? extends Contact> findContacts(String query) {
        return contactsService.findContactsByQuery(query);
    }

    public Response addNewContact(ContactDto contact) {
        return contactsService.addNewRecord(contact);
    }

    public Response delete(int recordIdx) {
        return contactsService.removeByIndex(recordIdx);
    }

    public Contact getByIndex(int recordIdx) {
        return contactsService.getByIndex(recordIdx);
    }

    public Response update(Contact contact, String field, String newValue) { // todo
        return contactsService.update(contact, field, newValue);
    }
}
