package contacts.client.activity;

import contacts.client.ActivityFactory;
import contacts.client.CommandLineUserInterface;
import contacts.client.ParsingUtils;
import contacts.controller.ContactsController;
import contacts.model.Contact;
import contacts.model.response.InvalidIndex;
import contacts.model.response.Response;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.List;

@AllArgsConstructor(onConstructor_ = {@Inject})
public class ListActivity implements Activity {

    private final CommandLineUserInterface cli;
    private final ContactsController contactsController;
    private final ActivityFactory activityFactory;

    @Override
    public void run() {
        List<? extends Contact> allContacts = contactsController.getAllContacts();
        if (allContacts.isEmpty()) {
            cli.send("No records to show!");
            return;
        }
        cli.printContactsList(allContacts);
        cli.emptyLine();

        while (true) {
            String cmd = cli.sendAndRead("[list] Enter action ([number], back):");
            if ("back".equals(cmd)) {
                return;
            }
            if (!ParsingUtils.isNumber(cmd)) {
                cli.invalidInput();
                continue;
            }
            final int number = Integer.parseInt(cmd);
            final int recordIdx = number - 1;
            if (recordIdx < 0 || recordIdx >= allContacts.size()) {
                cli.printStatus(Response.of(new InvalidIndex(recordIdx)));
                cli.emptyLine();
                continue;
            }
            Contact contact = allContacts.get(recordIdx);
            activityFactory.createRecordActivity(contact).run();
            break;
        }
    }
}
