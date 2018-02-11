package io.github.spair.byond.dmi;

public class DuplicateStateNameException extends Exception {

    private String dmiName;
    private final String duplicatedName;

    public DuplicateStateNameException(String message, String duplicatedName) {
        super(message);
        this.duplicatedName = duplicatedName;
    }

    public String getDmiName() {
        return dmiName;
    }

    void setDmiName(String dmiName) {
        this.dmiName = dmiName;
    }

    public String getDuplicatedName() {
        return duplicatedName;
    }
}
