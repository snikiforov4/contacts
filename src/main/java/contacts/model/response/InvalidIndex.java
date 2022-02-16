package contacts.model.response;

public final class InvalidIndex implements Status {

    private final int index;

    public InvalidIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
