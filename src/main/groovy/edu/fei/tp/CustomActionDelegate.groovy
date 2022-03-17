package edu.fei.tp

import com.netgrif.application.engine.auth.domain.RegisteredUser
import com.netgrif.application.engine.auth.web.requestbodies.NewUserRequest
import com.netgrif.application.engine.mail.interfaces.IMailService
import com.netgrif.application.engine.petrinet.domain.dataset.logic.action.ActionDelegate
import freemarker.template.TemplateException
import org.springframework.stereotype.Component

import javax.mail.MessagingException

@Component
class CustomActionDelegate extends ActionDelegate {
    void inviteUserForRegistration(String inviteMail){
        log.info("Inviting new user to group.");
        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.email = inviteMail;
        RegisteredUser regUser = registrationService.createNewUser(newUserRequest);
        userService.save(regUser);

        try {
            mailService.sendRegistrationEmail(regUser);
            mailAttemptService.mailAttempt(newUserRequest.email);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error(e.getMessage());
        }

    }
}
