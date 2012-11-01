package controllers;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import plugins.Neo4jPlugin;
import views.html.index;


public class Application extends Controller {

    private static Index<Node> getBarIndex() {
        return Neo4jPlugin.graphDb.index().forNodes("bars");
    }
    
    public static Result index() {
        return ok(index.render());
    }

    public static Result addBar() {
        Transaction tx = Neo4jPlugin.graphDb.beginTx();
        try {
            Node bar = Neo4jPlugin.graphDb.createNode();
            bar.setProperty("name", request().body().asFormUrlEncoded().get("name")[0]);
            getBarIndex().add(bar, "bars", "bars");
            tx.success();
        }
        finally {
            tx.finish();
        }
        
        return redirect(controllers.routes.Application.index());
    }

    public static Result listBars() {
        IndexHits<Node> barNodes = Neo4jPlugin.graphDb.index().forNodes("bars").get("bars", "bars");

        ArrayNode bars = new ObjectMapper().createArrayNode();

        while (barNodes.hasNext()) {
            ObjectNode bar = Json.newObject();
            bar.put("name", (String) barNodes.next().getProperty("name"));
            bars.add(bar);
        }
        
        return ok(bars);
    }
}