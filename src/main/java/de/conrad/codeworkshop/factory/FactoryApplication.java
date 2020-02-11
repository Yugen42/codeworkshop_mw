package de.conrad.codeworkshop.factory;

import de.conrad.codeworkshop.factory.services.factory.AsynchronousWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * @author Andreas Hartmann
 */
@SpringBootApplication
public class FactoryApplication {

    @Autowired
    private AsynchronousWorker worker;


    @EventListener
    public void onStartup(ApplicationReadyEvent event){
        worker.run();
    }
}
