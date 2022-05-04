package edu.fei.tp.startup

import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CsvImportRunner extends AbstractOrderedCommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(CsvImportRunner.class)

    @Override
    void run(String... strings) throws Exception {
        // todo implement csv importing
    }
}
