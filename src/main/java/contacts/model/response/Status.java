package contacts.model.response;

sealed public interface Status permits InvalidIndex, Modified, WrongFieldName, WrongNumberFormat {
}
