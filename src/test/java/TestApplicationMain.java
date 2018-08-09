import com.kuohao.learn.spring.Application;
import com.kuohao.learn.spring.MyBanner;
import com.kuohao.learn.spring.listener.MyApplicationEnvironmentPreparedEventListener;
import com.kuohao.learn.spring.listener.MyApplicationStartedEventListener;
import com.kuohao.learn.spring.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/30
 * Time: 下午3:46
 * CopyRight: taobao
 * Descrption:
 */
@SpringBootApplication
@EnableConfigurationProperties
public class TestApplicationMain {
    public static void main(String[] args) {
        //        SpringApplication.run(Application.class, args);
                SpringApplication app = new SpringApplication(Application.class);
                //添加自定义listner
                app.addListeners(new MyApplicationStartedEventListener());
                app.addListeners(new MyApplicationEnvironmentPreparedEventListener());
                app.setBanner(new MyBanner());
                app.run(args);
    }




}
