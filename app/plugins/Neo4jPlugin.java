package plugins;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import play.Application;
import play.Logger;
import play.Plugin;

public class Neo4jPlugin extends Plugin {

    private final Application application;

    public static GraphDatabaseService graphDb;


    public Neo4jPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {

        if (application.configuration().getString("neo4j.resturl") != null) {
            graphDb = new RestGraphDatabase(application.configuration().getString("neo4j.resturl"));
            Logger.info("Using Neo4j via REST: " + application.configuration().getString("neo4j.resturl"));
        }
        else {
            String path = "target/neo4j";
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
            Logger.info("Using Neo4j embedded: " + path);
        }
        
    }

    @Override
    public void onStop() {
        graphDb.shutdown();
    }
    
}
