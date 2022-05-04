package edu.fei.tp

import com.netgrif.application.engine.auth.domain.IUser
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.delegate.RoleActionDelegate
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.workflow.domain.Case
import com.netgrif.application.engine.workflow.domain.QCase
import com.netgrif.application.engine.workflow.service.interfaces.IWorkflowService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomRoleActionDelegate extends RoleActionDelegate {

    @Autowired
    private IPetriNetService petriNetService

    @Autowired
    private IWorkflowService workflowService

    boolean checkIfUserHasInstance(String processIdentifier, IUser user = (IUser)affectedUser){
        Case foundCase = workflowService.searchOne(QCase.case$.author.id.eq(user.getStringId()) & QCase.case$.processIdentifier.eq(processIdentifier))
        return foundCase != null
    }

    Case createInstance(String processIdentifier, String title, String color, IUser user = (IUser)affectedUser){
        String netId = petriNetService.getByIdentifier(processIdentifier)?.get(0)?.stringId
        if(netId != null){
            Case createdCase = workflowService.createCase(netId, title, color, user.transformToLoggedUser()).getCase()
            workflowService.save(createdCase)
            return createdCase
        }
        else {
            return null
        }
    }
}
