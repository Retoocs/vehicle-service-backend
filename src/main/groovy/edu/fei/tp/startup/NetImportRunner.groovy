package edu.fei.tp.startup

import com.netgrif.application.engine.importer.model.Document
import com.netgrif.application.engine.petrinet.domain.throwable.MissingPetriNetMetaDataException
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.startup.ImportHelper
import org.drools.core.io.impl.ClassPathResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import edu.fei.tp.helpers.NetEnum
import javax.xml.bind.JAXBContext

@Component
class NetImportRunner extends AbstractOrderedCommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(NetImportRunner.class)

    @Autowired
    private ImportHelper helper

    @Autowired
    private IPetriNetService petriNetService

    @Override
    void run(String... strings) throws Exception {
        def nets = []
        nets.addAll(NetEnum.values().toList().collect { it.netFile })
        nets.collate(10).each {
            upsert(it as List<String>)
        }
    }

    private boolean upsert(String netName, String version = "patch") {
        try {
            InputStream netStream = new ClassPathResource("petriNets/$netName" as String).inputStream
            JAXBContext jaxbContext = JAXBContext.newInstance(Document.class)
            Document document = (Document) jaxbContext.createUnmarshaller().unmarshal(netStream)
            if (!petriNetService.getNewestVersionByIdentifier(document.id))
                helper.createNet(netName as String, version)
                        .orElseThrow({ new IllegalArgumentException("Petri net " + netName + " was not created!") })
            return true
        } catch (MissingPetriNetMetaDataException e) {
            log.error("Properties from the imported net: ${netName} are missing", e)
            return false
        }
    }

    private void upsert(List<String> nets) {
        def threads = []
        nets.each { netName ->
            threads << Thread.start {
                upsert(netName, "major")
            }
        }
        threads.each { it.join() }
    }
}
