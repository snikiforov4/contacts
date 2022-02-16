package contacts.model.response;

public sealed interface Modified extends Status permits Added, Removed, Updated {
}
