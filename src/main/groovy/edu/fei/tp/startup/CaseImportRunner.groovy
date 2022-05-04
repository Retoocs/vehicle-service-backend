package edu.fei.tp.startup

import com.netgrif.application.engine.auth.domain.IUser
import com.netgrif.application.engine.auth.service.interfaces.IUserService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.workflow.domain.Case
import com.netgrif.application.engine.workflow.service.interfaces.IWorkflowService
import edu.fei.tp.CustomActionDelegate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.netgrif.application.engine.workflow.domain.QCase

@Component
class CaseImportRunner extends AbstractOrderedCommandLineRunner{

    public static final Logger log = LoggerFactory.getLogger(CaseImportRunner.class)

    @Autowired
    private IUserService userService

    @Autowired
    private IWorkflowService workflowService

    @Autowired
    private CustomActionDelegate actionDelegate

    @Override
    void run(String... strings) throws Exception {
        createEnumCases()
        createNeededInstances()
    }

    private void createEnumCases(){
        def orderedCreateList = ["main_enum_addresses", "main_enum_vehicles"]
        IUser engineUser = userService.findByEmail("engine@netgrif.com", true)

        orderedCreateList.forEach( netId -> {
            Case existingEngineCase = workflowService.searchOne(QCase.case$.author.id.eq(engineUser.getStringId()).and(QCase.case$.processIdentifier.eq(netId)))
            if(existingEngineCase == null) {
                if (!createCaseByNetId(netId, netId, "gray")) {
                    log.error("Could not create instance of process: " + netId)
                }
            }
        })
    }

    private Case createCaseByNetId(String netId, String title, String color, IUser author = userService.findByEmail("engine@netgrif.com", true)){
        return actionDelegate.createCase(netId, title, color, author)
    }

    private void createNeededInstances(){
        def orderedCreateList = ["child_enum_addresses", "child_enum_vehicles"]
        IUser superUser = userService.findByEmail("super@netgrif.com", true)
        IUser timakUser = userService.findByEmail("timak.projekt@gmail.com", true)

        orderedCreateList.forEach( netId -> {
            Case existingSuperCase = workflowService.searchOne(QCase.case$.author.id.eq(superUser.getStringId()).and(QCase.case$.processIdentifier.eq(netId)))
            Case existingTimakCase = workflowService.searchOne(QCase.case$.author.id.eq(timakUser.getStringId()).and(QCase.case$.processIdentifier.eq(netId)))

            if(existingSuperCase == null) {
                if (!createCaseByNetId(netId, netId, "white", userService.findByEmail("super@netgrif.com", true))) {
                    log.error("Could not create instance of process: " + netId + " for user: super@netgrif.com")
                }
            }
            if(existingTimakCase == null) {
                if (!createCaseByNetId(netId, netId, "white", userService.findByEmail("timak.projekt@gmail.com", true))) {
                    log.error("Could not create instance of process: " + netId + " for user: timak.projekt@gmail.com")
                }
            }
        })
    }
}
