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

        forTesting()
    }

    private void createEnumCases(){
        def orderedCreateList = ["main_enum_addresses", "main_enum_vehicles"]
        orderedCreateList.forEach( netId -> {
            if(!createCaseByNetId(netId, netId, "gray")){
                log.error("Could not create instance of process: " + netId)
            }
        })
    }

    private Case createCaseByNetId(String netId, String title, String color, IUser author = userService.findByEmail("engine@netgrif.com", true)){
        return actionDelegate.createCase(netId, title, color, author)
    }

    /** temporary */
    private void forTesting(){
        def orderedCreateList = ["vehicle", "customer", "child_enum_addresses", "child_enum_vehicles"]

        orderedCreateList.forEach( netId -> {
            if(!createCaseByNetId(netId, netId, "white", userService.findByEmail("super@netgrif.com", true))){
                log.error("Could not create instance of process: " + netId)
            }
        })
    }
}
