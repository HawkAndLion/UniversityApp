package ua.foxminded.university.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Course {
    private int id;
    private String name;
    private String description;

    public Course(String courseName, String courseDescription) {
        super();
        this.name = courseName;
        this.description = courseDescription;
    }
}
