package contacts.model;


import contacts.service.EditableField;
import contacts.service.SearchableField;

import java.time.Instant;

public abstract sealed class Contact permits OrganizationContact, PersonContact {

    @SearchableField
    @EditableField(name = "number")
    private String phoneNumber;
    private Instant creationDate;
    private Instant lastModifiedDate;

    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isEmpty();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
