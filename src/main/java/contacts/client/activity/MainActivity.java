package contacts.client.activity;

import contacts.client.ActivityFactory;
import contacts.client.CommandLineUserInterface;
import contacts.client.ParsingUtils;
import contacts.controller.ContactsController;
import contacts.controller.dto.OrganizationContactDto;
import contacts.controller.dto.PersonContactDto;
import contacts.model.Contact;
import contacts.model.response.InvalidIndex;
import contacts.model.response.Response;
import contacts.service.UnhandledType;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@AllArgsConstructor(onConstructor_ = {@Inject})
public class MainActivity implements Activity {

    private static final String PERSON_TYPE = "person";
    private static final String ORGANIZATION_TYPE = "organization";
    private static final Set<String> ALLOWED_TYPES = Set.of(PERSON_TYPE, ORGANIZATION_TYPE);

    private final CommandLineUserInterface cli;
    private final ContactsController contactsController;
    private final ActivityFactory activityFactory;

    @Override
    public void run() {
        while (true) {
            String cmd = cli.sendAndRead("[menu] Enter action (add, list, search, count, exit):");
            if ("exit".equals(cmd)) {
                break;
            }
            switch (cmd) {
                case "add" -> addContact();
                case "list" -> activityFactory.createListActivity().run();
                case "search" -> search();
                case "count" -> cli.send("The Phone Book has %d records.".formatted(contactsController.count()));
                default -> cli.invalidInput();
            }
            cli.emptyLine();
        }
    }

    private void addContact() {
        String type = cli.sendAndRead("Enter the type: %s"
                .formatted(String.join(", ", ALLOWED_TYPES)));
        Response response = switch (type) {
            case PERSON_TYPE -> {
                String name = cli.sendAndRead("Enter the name of the person:");
                String surname = cli.sendAndRead("Enter the surname of the person:");
                String birthDate = cli.sendAndRead("Enter the birth date:");
                String gender = cli.sendAndRead("Enter gender: (M, F)");
                String phoneNumber = cli.sendAndRead("Enter the number:");
                PersonContactDto contact = new PersonContactDto();
                contact.setName(name);
                contact.setSurname(surname);
                contact.setPhoneNumber(phoneNumber);
                contact.setBirthDate(birthDate);
                contact.setGender(gender);
                yield contactsController.addNewContact(contact);
            }
            case ORGANIZATION_TYPE -> {
                String organizationName = cli.sendAndRead("Enter the organization name:");
                String address = cli.sendAndRead("Enter the address:");
                String phoneAddress = cli.sendAndRead("Enter the number:");
                OrganizationContactDto contact = new OrganizationContactDto();
                contact.setOrganizationName(organizationName);
                contact.setAddress(address);
                contact.setPhoneNumber(phoneAddress);
                yield contactsController.addNewContact(contact);
            }
            default -> throw new UnhandledType(type);
        };
        cli.printStatus(response);
    }

    private void search() {
        outer:
        do {
            String query = cli.sendAndRead("Enter search query:");
            List<? extends Contact> foundContacts = contactsController.findContacts(query);
            if (foundContacts.isEmpty()) {
                cli.send("No records found!");
            } else {
                cli.send("Found %d result:".formatted(foundContacts.size()));
                cli.printContactsList(foundContacts);
            }
            cli.emptyLine();
            while (true) {
                String cmd = cli.sendAndRead("[search] Enter action ([number], back, again):");
                if ("back".equals(cmd)) {
                    return;
                }
                if ("again".equals(cmd)) {
                    continue outer;
                }
                if (!ParsingUtils.isNumber(cmd)) {
                    cli.invalidInput();
                    continue;
                }
                final int number = Integer.parseInt(cmd);
                final int recordIdx = number - 1;
                if (recordIdx < 0 || recordIdx >= foundContacts.size()) {
                    cli.printStatus(Response.of(new InvalidIndex(recordIdx)));
                    cli.emptyLine();
                    continue;
                }
                Contact contact = foundContacts.get(recordIdx);
                activityFactory.createRecordActivity(contact).run();
                break outer;
            }
        } while (true);
    }
}
