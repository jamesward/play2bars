import com.avaje.ebean.Ebean;
import models.Bar;
import play.Application;
import play.GlobalSettings;
import play.api.db.evolutions.OfflineEvolutions;
import play.libs.Yaml;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

    public void beforeStart(Application app) {
        File applicationPath = app.path();
        OfflineEvolutions.applyScript(applicationPath, app.classloader(), "default");

    }

    public void onStart(Application app) {
        InitialData.insert(app);
    }

    static class InitialData {

        public static void insert(Application app) {

            if (Ebean.find(Bar.class).findRowCount() == 0) {

                Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load(
                        app.resourceAsStream("conf/initial-data.yml"),
                        app.classloader()
                );

                List<Object> bars = all.get("bars");
                Ebean.save(bars);

            }
        }

    }

}
