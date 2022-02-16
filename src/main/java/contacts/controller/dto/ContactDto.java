package contacts.controller.dto;

public sealed interface ContactDto permits OrganizationContactDto, PersonContactDto {
    String getPhoneNumber();
}
