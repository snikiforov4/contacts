package contacts.model;

import contacts.service.EditableField;
import contacts.service.SearchableField;

public final class OrganizationContact extends Contact {
    @SearchableField
    @EditableField(name = "organization name")
    private String organizationName;
    @SearchableField
    @EditableField
    private String address;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
