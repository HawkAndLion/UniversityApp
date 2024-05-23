package ua.foxminded.university.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Group {
    private int id;
    private String name;

    public Group() {
        super();
    }

    public Group(String groupName) {
        this.name = groupName;
    }
}
