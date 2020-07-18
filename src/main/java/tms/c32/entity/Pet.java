package tms.c32.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    private int id;
    private Category Category;
    private String name;
    private List<Tags> tags;
    private Status status;
}
