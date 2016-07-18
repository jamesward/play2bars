package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Bar extends Model {

    @Id
    public UUID id;

    public String name;

    public static Finder<String, Bar> find = new Finder<>(Bar.class);

}
