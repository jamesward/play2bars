package plugins;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import play.Application;
import play.Logger;
import play.Plugin;

import java.net.URI;
import java.net.URISyntaxException;

public class Neo4jPlugin extends Plugin {

    private final Application application;

    public static GraphDatabaseService graphDb;


    public Neo4jPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        
        URI restURI = null;
        
        try {
            restURI = new URI(application.configuration().getString("neo4j.resturl"));
        } catch (Exception e) {
            // ignored
        }
        
        if (restURI != null) {
            String username = restURI.getUserInfo().split(":")[0];
            String password = restURI.getUserInfo().split(":")[1];
            graphDb = new RestGraphDatabase(restURI.toString(), username, password);
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
