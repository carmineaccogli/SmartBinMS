package it.unisalento.pas.smartcitywastemanagement.smartbinms.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("type")
public class Type {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private String color;





    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
