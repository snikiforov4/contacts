package contacts.service;

import contacts.controller.dto.ContactDto;
import contacts.controller.dto.OrganizationContactDto;
import contacts.controller.dto.PersonContactDto;
import contacts.model.Contact;
import contacts.model.OrganizationContact;
import contacts.model.PersonContact;
import contacts.model.response.*;
import contacts.repository.ContactsRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@AllArgsConstructor
public class ContactsService {

    private final ContactsRepository repository;
    private final PhoneNumberValidator phoneNumberValidator;
    private final ClassFieldSetterProvider classFieldSetterProvider;

    public long count() {
        return repository.count();
    }

    public List<? extends Contact> getAllContacts() {
        return repository.getAll();
    }

    public Response addNewRecord(ContactDto contactDto) {
        Response response = Response.empty();
        Contact contact = switch (contactDto) {
            case PersonContactDto pcDto -> {
                PersonContact personContact = new PersonContact();
                personContact.setName(pcDto.getName());
                personContact.setSurname(pcDto.getSurname());
                personContact.setBirthDate(pcDto.getBirthDate());
                personContact.setGender(pcDto.getGender());
                yield personContact;
            }
            case OrganizationContactDto ocDto -> {
                OrganizationContact organizationContact = new OrganizationContact();
                organizationContact.setOrganizationName(ocDto.getOrganizationName());
                organizationContact.setAddress(ocDto.getAddress());
                yield organizationContact;
            }
        };
        contact.setPhoneNumber(validatePhoneNumber(response, contactDto));
        Instant now = Instant.now();
        contact.setCreationDate(now);
        contact.setLastModifiedDate(now);
        repository.save(contact);
        response.add(new Added());
        return response;
    }

    private String validatePhoneNumber(Response response, ContactDto pcDto) {
        String phoneNumber = pcDto.getPhoneNumber();
        if (!phoneNumberValidator.isValid(phoneNumber)) {
            response.add(new WrongNumberFormat());
            phoneNumber = "";
        }
        return phoneNumber;
    }

    public Contact getByIndex(int index) {
        return repository.getByIndex(index);
    }

    public Response removeByIndex(int index) {
        boolean removed = repository.removeByIndex(index);
        Response response;
        if (removed) {
            response = Response.of(new Removed());
        } else {
            response = Response.of(new InvalidIndex(index));
        }
        return response;
    }

    public Response update(Contact contact, String field, String value) {
        Response response = Response.empty();
        if ("number".equals(field)) {
            String number = "";
            if (phoneNumberValidator.isValid(value)) {
                number = value;
            } else {
                response.add(new WrongNumberFormat());
            }
            contact.setPhoneNumber(number);
        } else {
            classFieldSetterProvider.forClass(contact.getClass()).updateField(contact, field, value);
        }
        contact.setLastModifiedDate(Instant.now());
        response.add(new Updated());
        return response;
    }

    @SneakyThrows
    public List<? extends Contact> findContactsByQuery(String query) {
        List<Contact> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        for (Contact contact : repository.getAll()) {
            StringBuilder builder = new StringBuilder();
            for (Field field : classFieldSetterProvider.forClass(contact.getClass()).getSearchableFields()) {
                field.setAccessible(true);
                builder.append(field.get(contact));
            }
            boolean matches = pattern.matcher(builder.toString()).find();
            if (matches) {
                result.add(contact);
            }
        }
        return result;
    }
}
