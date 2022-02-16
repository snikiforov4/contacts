package contacts.client;

import contacts.model.Contact;
import contacts.model.OrganizationContact;
import contacts.model.PersonContact;
import contacts.model.response.*;

import java.io.PrintStream;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

import static contacts.controller.ContactsController.FORMATTER;

public class CommandLineUserInterface {

    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = System.out;

    public void send(String text) {
        out.println(text);
    }

    public void emptyLine() {
        out.println();
    }

    public void invalidInput() {
        out.println("Invalid input!");
    }

    public String read() {
        return in.nextLine();
    }

    public String sendAndRead(String text) {
        send(text);
        return read();
    }

    public <E extends Contact> void printContactsList(List<E> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            switch (contact) {
                case PersonContact pc ->
                        send(String.format("%d. %s %s", i + 1, pc.getName(), pc.getSurname()));
                case OrganizationContact oc ->
                        send(String.format("%d. %s", i + 1, oc.getOrganizationName()));
            }
        }
    }

    public <E extends Contact> void printContact(E contact) {
        switch (contact) {
            case PersonContact pc -> {
                send("Name: " + orNoData(pc.getName()));
                send("Surname: " + orNoData(pc.getSurname()));
                send("Birth date: " + orNoData(pc.getBirthDate()));
                send("Gender: " + orNoData(pc.getGender()));
            }
            case OrganizationContact oc -> {
                send("Organization name: " + orNoData(oc.getOrganizationName()));
                send("Address: " + orNoData(oc.getAddress()));
            }
        }
        send("Number: " +  orNoData(contact.getPhoneNumber()));
        send("Time created: " +  orNoData(contact.getCreationDate()));
        send("Time last edit: 2018-01-01T00:00" +  orNoData(contact.getLastModifiedDate()));
    }

    private String orNoData(Object object) {
        String result = "[no data]";
        String stringRepresentation;
        if (object != null && !((stringRepresentation = object.toString()).isBlank())) {
            result = stringRepresentation;
        }
        return result;
    }

    private String orNoData(Instant date) {
        String result = "[no data]";
        if (date != null) {
            result = FORMATTER.format(date);
        }
        return result;
    }

    // todo print message?
    public void printStatus(Response response) {
        for (Status status : response.getStatuses()) {
            switch (status) {
                case Added added -> send("The record added.");
                case Updated updated -> send("The record updated!");
                case Removed removed -> send("The record removed!");
                case WrongFieldName wfn -> send("Wrong field name: %s!".formatted(wfn.getField()));
                case InvalidIndex ici -> send("Wrong contacts index: %s!".formatted(ici.getIndex()));
                case WrongNumberFormat wnf -> send("Wrong number format!");
            }
        }
    }
}
