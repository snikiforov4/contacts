package contacts.service;

public class UnhandledType extends RuntimeException {
    private final String type;

    public UnhandledType(String type) {
        this.type = type;
    }
}
