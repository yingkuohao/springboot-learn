import com.kuohao.learn.spring.Application;
import com.kuohao.learn.spring.config.ServiceSettings;
import com.kuohao.learn.spring.config.SysTemVO;
import com.kuohao.learn.spring.service.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/26
 * Time: 下午3:33
 * CopyRight: taobao
 * Descrption:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
//@SpringApplicationConfiguration(classes = SpringBootSampleApplication.class)
@WebIntegrationTest("server.port:0")
public class MQTest {

    @Autowired
    EmailService emailService;
    @Autowired
    SysTemVO sysTemVO;

    @Autowired
    Environment env;

    @Test
    public void testConsumeMsg() {
        emailService.sendEmail();
        System.out.println("getProducerId=" + ServiceSettings.getProducerId());
        System.out.println("systemVo=" + sysTemVO.toString());
        String str = env.getProperty("spring.datasource.url");
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        Environment env1 = webApplicationContext.getEnvironment();
        String str2 = env1.getProperty("spring.datasource.url");
        System.out.println(str);
        System.out.println(str2);

    }

}
