package contacts.service;

import contacts.model.OrganizationContact;
import contacts.model.PersonContact;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;


class ClassFieldSetterTest {

    @Nested
    @DisplayName("Class PersonContacts")
    class PersonContactsTest {
        @Test
        void returnsAllEditableFieldNames() {
            Set<String> expectedNames = Set.of("name", "surname", "number", "birthdate", "gender");
            ClassFieldSetter sut = ClassFieldSetter.forClass(PersonContact.class);

            Set<String> actualNames = sut.getAllEditableFieldNames();

            Assertions.assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames);
        }

        @Test
        void returnsAllSearchableFields() {
            Set<String> expectedNames = Set.of("name", "surname", "phoneNumber");
            ClassFieldSetter sut = ClassFieldSetter.forClass(PersonContact.class);

            Set<String> actualNames = sut.getSearchableFields()
                    .stream()
                    .map(Field::getName)
                    .collect(Collectors.toUnmodifiableSet());

            Assertions.assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames);
        }
    }

    @Nested
    @DisplayName("Class PersonContacts")
    class OrganizationContactsTest {
        @Test
        void returnsAllEditableFieldNames() {
            Set<String> expectedNames = Set.of("organization name", "address", "number");
            ClassFieldSetter sut = ClassFieldSetter.forClass(OrganizationContact.class);

            Set<String> actualNames = sut.getAllEditableFieldNames();

            Assertions.assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames);
        }

        @Test
        void returnsAllSearchableFields() {
            Set<String> expectedNames = Set.of("organizationName", "address", "phoneNumber");
            ClassFieldSetter sut = ClassFieldSetter.forClass(OrganizationContact.class);

            Set<String> actualNames = sut.getSearchableFields()
                    .stream()
                    .map(Field::getName)
                    .collect(Collectors.toUnmodifiableSet());

            Assertions.assertThat(actualNames).containsExactlyInAnyOrderElementsOf(expectedNames);
        }
    }

}