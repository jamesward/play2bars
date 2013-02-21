import configs.AppConfig;
import models.Bar;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import services.BarService;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

@ContextConfiguration(classes={AppConfig.class, TestDataConfig.class})
public class BarServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private BarService barService;

    @Test
    public void createBar() {
        Bar bar = new Bar();
        bar.name = "foo";
        barService.addBar(bar);
        assertThat(bar.id).isNotNull();
    }

    @Test
    public void getBars() {
        createBar();
        List<Bar> bars = barService.getAllBars();
        assertThat(bars.size()).isEqualTo(1);
    }

}