package edu.fei.tp.startup

import com.netgrif.application.engine.auth.domain.Authority
import com.netgrif.application.engine.auth.domain.IUser
import com.netgrif.application.engine.auth.domain.User
import com.netgrif.application.engine.auth.domain.UserState
import com.netgrif.application.engine.auth.service.interfaces.IAuthorityService
import com.netgrif.application.engine.auth.service.interfaces.IUserService
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import edu.fei.tp.helpers.NetEnum
import edu.fei.tp.helpers.VSUserManagmentHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserRunner extends AbstractOrderedCommandLineRunner{

    public static final Logger log = LoggerFactory.getLogger(UserRunner.class)

    @Autowired
    private IUserService userService

    @Autowired
    private IAuthorityService authorityService

    @Autowired
    private IPetriNetService petriNetService

    @Autowired
    private VSUserManagmentHelper userManagmentHelper

    @Value("\${timak.user.email}")
    private String timakEmail
    @Value("\${timak.user.firstname}")
    private String timakFirstname
    @Value("\${timak.user.lastname}")
    private String timakLastname
    @Value("\${timak.user.password}")
    private String timakPassword
    private Map<String, List<String>> timakRoleMapping = [
            (NetEnum.CEA.netIdentifier): [],
            (NetEnum.CEV.netIdentifier): [],
            (NetEnum.MEA.netIdentifier): [],
            (NetEnum.MEV.netIdentifier): [],
            (NetEnum.RTW.netIdentifier): [],
            (NetEnum.WTR.netIdentifier): [],
            (NetEnum.CUS.netIdentifier): ["mechanic"],
            (NetEnum.NTF.netIdentifier): [],
            (NetEnum.RPR.netIdentifier): ["mechanic","anonym","otherworker"],
            (NetEnum.RI.netIdentifier): ["mechanic"],
            (NetEnum.VEH.netIdentifier): ["mechanic"],
            (NetEnum.WH.netIdentifier): ["mechanic"],
            (NetEnum.WI.netIdentifier): ["mechanic"],
    ]


    @Override
    void run(String... strings) throws Exception {
        addAllRolesToSuper()
        createTimakUser()
    }

    private void addAllRolesToSuper(){
        IUser superUser = userService.findByEmail("super@netgrif.com", true)

        if (superUser == null) {
            log.error("Could not find super user")
            return
        }

        def rolesOfPetriNets = petriNetService.getAll().collect {it.roles.values()}
        rolesOfPetriNets.each {roles ->
            roles.each {superUser.addProcessRole(it)}
        }
        userService.save(superUser)

        log.info("Added all roles to super@netgrif.com.")
    }

    private void createTimakUser(){
        log.info("Creating Timak Projekt user...")

        Authority adminAuthority = authorityService.getOrCreate(Authority.admin)
        Authority userAuthority = authorityService.getOrCreate(Authority.user)

        User user = new User()
        user.setEmail(this.timakEmail)
        user.setName(this.timakFirstname)
        user.setSurname(this.timakLastname)
        user.setPassword(this.timakPassword)
        user.setState(UserState.ACTIVE)
        user.setAuthorities([adminAuthority, userAuthority] as Set<Authority>)
        user = userManagmentHelper.addProcessRolesToUser(this.timakRoleMapping, user)

        userService.saveNew(user)
        log.info("Created Timak Projekt user")
    }



}
