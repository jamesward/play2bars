import play.Application;
import play.GlobalSettings;
import play.api.db.evolutions.OfflineEvolutions;

import java.io.File;

public class Global extends GlobalSettings {

    public void beforeStart(Application app) {
        File applicationPath = app.path();
        OfflineEvolutions.applyScript(applicationPath, app.classloader(), "default");

    }

    public void onStart(Application app) {
    }


}
