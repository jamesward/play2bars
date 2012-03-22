package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bar extends Model {

    @Id
    public String id;

    public String name;

    public static Model.Finder<String, Bar> find = new Model.Finder(String.class, Bar.class);

}