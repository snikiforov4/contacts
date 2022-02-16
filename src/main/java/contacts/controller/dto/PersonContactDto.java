package contacts.controller.dto;

import lombok.Data;

@Data
public final class PersonContactDto implements ContactDto {
    String name;
    String surname;
    String phoneNumber;
    String birthDate;
    String gender;
}
