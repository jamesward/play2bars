package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class Bar extends Model {

    public String name;

    // -- Queries
    public static Model.Finder<String, Bar> find = new Model.Finder(String.class, Bar.class);

    /**
     * Retrieve all users.
     */
    public static List<Bar> findAll() {
        return find.all();
    }


}