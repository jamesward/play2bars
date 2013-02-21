package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bar {

    @Id
    @GeneratedValue
    public String id;

    @Constraints.Required(message = "The name is required")
    public String name;

}