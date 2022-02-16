package contacts.model;

import contacts.service.EditableField;
import contacts.service.SearchableField;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static contacts.controller.ContactsController.FORMATTER;

public final class PersonContact extends Contact {
    @SearchableField
    @EditableField
    private String name;
    @SearchableField
    @EditableField
    private String surname;
    @EditableField(name = "birthdate")
    private Instant birthDate;
    @EditableField
    private Gender gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        setBirthDate(parseBirthDate(birthDate));
    }

    private Instant parseBirthDate(String birthDate) {
        try {
            return FORMATTER.parse(birthDate, Instant::from);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        setGender(parseGender(gender));
    }

    private Gender parseGender(String gender) {
        if (gender.length() != 1) {
            return null;
        }
        return Gender.fromNotation(gender.charAt(0));
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
