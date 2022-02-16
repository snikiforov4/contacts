package contacts.client.activity;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import contacts.client.CommandLineUserInterface;
import contacts.controller.ContactsController;
import contacts.model.Contact;
import contacts.model.response.Response;
import contacts.model.response.WrongFieldName;
import contacts.service.ClassFieldSetterProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecordActivity implements Activity {

    private final CommandLineUserInterface cli;
    private final ContactsController contactsController;
    private final ClassFieldSetterProvider classFieldSetterProvider;
    private final Contact contact;

    @Inject
    public RecordActivity(CommandLineUserInterface cli,
                          ContactsController contactsController,
                          ClassFieldSetterProvider classFieldSetterProvider,
                          @Assisted Contact contact) {
        this.cli = cli;
        this.contactsController = contactsController;
        this.classFieldSetterProvider = classFieldSetterProvider;
        this.contact = contact;
    }

    @Override
    public void run() {
        cli.printContact(contact);
        outer:
        while (true) {
            cli.emptyLine();
            String cmd = cli.sendAndRead("[record] Enter action (edit, delete, menu):");
            switch (cmd) {
                case "menu" -> {
                    break outer;
                }
                case "edit" -> {
                    edit(contact);
                    cli.emptyLine();
                    cli.printContact(contact);
                }
                case "delete" -> {
                    delete();
                    break outer;
                }
                default -> cli.invalidInput();
            }
        }
    }

    private void edit(Contact contact) {
        Set<String> fieldsForEdit = fieldAvailableForEdit(contact);
        String field = cli.sendAndRead("Select a field %s:".formatted(fieldsToString(fieldsForEdit)));
        if (!fieldsForEdit.contains(field)) {
            cli.printStatus(Response.of(new WrongFieldName(field)));
            return;
        }
        String newValue = cli.sendAndRead("Enter %s: ".formatted(field));
        Response response = contactsController.update(contact, field, newValue);
        cli.printStatus(response);
    }


    private Set<String> fieldAvailableForEdit(Contact contact) {
        return classFieldSetterProvider.forClass(contact.getClass()).getAllEditableFieldNames();
    }

    private String fieldsToString(Set<String> fields) {
        return fields.stream().collect(Collectors.joining(", ", "(", ")"));
    }

    private void delete() {
        List<? extends Contact> allContactsForRemove = contactsController.getAllContacts();
        if (allContactsForRemove.isEmpty()) {
            cli.send("No records to remove!");
            return;
        }
        cli.printContactsList(allContactsForRemove);
        final int recordNumber = Integer.parseInt(cli.sendAndRead("Select a record:")); //todo more elegant
        final int recordIdx = recordNumber - 1;
        Response response = contactsController.delete(recordIdx);
        cli.printStatus(response);
    }
}
