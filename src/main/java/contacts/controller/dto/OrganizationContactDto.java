package contacts.controller.dto;

import lombok.Data;

@Data
public final class OrganizationContactDto implements ContactDto {
    String organizationName;
    String address;
    String phoneNumber;
}
