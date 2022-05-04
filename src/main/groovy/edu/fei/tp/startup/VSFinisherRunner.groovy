package edu.fei.tp.startup

import com.netgrif.application.engine.startup.FinisherRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class VSFinisherRunner extends FinisherRunner{
    public static final Logger log = LoggerFactory.getLogger(VSFinisherRunner.class)

    @Override
    void run(String... strings) throws Exception {
        log.info("+-----------------------------+")
        log.info("| Vehicle Service Application |")
        log.info("+-----------------------------+")
    }
}
