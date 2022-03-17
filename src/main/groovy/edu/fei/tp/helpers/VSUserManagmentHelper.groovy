package edu.fei.tp.helpers

import com.netgrif.application.engine.auth.domain.User
import com.netgrif.application.engine.petrinet.domain.roles.ProcessRole
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.petrinet.service.interfaces.IProcessRoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class VSUserManagmentHelper {
    @Autowired
    private IProcessRoleService processRoleService

    @Autowired
    private IPetriNetService petriNetService

    User addProcessRolesToUser(Map<String, List<String>> roleMapping, User user){

        Set<ProcessRole> processRolesToAssign = [] as Set<ProcessRole>
        for(mappingEntry in roleMapping){
            if(mappingEntry.value.isEmpty())
                continue

            String netId = petriNetService.findByImportId(mappingEntry.key).get().stringId
            for(processRoleInNet in processRoleService.findAll(netId)){
                if(mappingEntry.value.contains(processRoleInNet.importId))
                    processRolesToAssign.add(processRoleInNet)
            }
        }

        user.setProcessRoles(processRolesToAssign)
        return user
    }
}
