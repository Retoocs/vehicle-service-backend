package edu.fei.tp;

import com.netgrif.application.engine.ApplicationEngine;
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate;
import com.netgrif.application.engine.startup.RunnerController;
import edu.fei.tp.startup.CustomRunnerController;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableCaching
@SpringBootApplication
@ComponentScan({"com.netgrif.application.engine", "edu.fei.tp"})
public class VehicleServiceApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ApplicationEngine.class, VehicleServiceApp.class)
                .run(args);
    }

    @Bean
    @Primary
    public RunnerController runnerController() {
        return new CustomRunnerController();
    }

    @Primary
    @Bean("actionDelegate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ActionDelegate actionDelegate() {
        return new CustomActionDelegate();
    }
}



