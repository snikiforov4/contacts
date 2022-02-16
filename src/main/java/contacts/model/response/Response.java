package contacts.model.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Response {

    private final List<Status> statuses;

    private Response() {
        statuses = new ArrayList<>();
    }

    public static Response empty() {
        return new Response();
    }

    public static Response of(Status status) {
        Response response = new Response();
        response.add(status);
        return response;
    }

    public List<Status> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    public void add(Status status) {
        statuses.add(status);
    }
}
