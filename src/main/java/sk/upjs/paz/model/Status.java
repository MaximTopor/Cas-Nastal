package sk.upjs.paz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
    private long idStatus;
    private String name;
    private String description;
    private String createdAt;

    @Override
    public String toString() {
        return name;
    }
}