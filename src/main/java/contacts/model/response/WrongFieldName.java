package contacts.model.response;

public final class WrongFieldName implements Status {

    private final String field;

    public WrongFieldName(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
