package contacts.model;

public enum Gender {
    Male('M'),
    Female('F');

    private final char notation;

    Gender(char notation) {
        this.notation = notation;
    }

    public static Gender fromNotation(char notation) {
        for (Gender gender : values()) {
            if (gender.notation == notation) {
                return gender;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(notation);
    }
}
