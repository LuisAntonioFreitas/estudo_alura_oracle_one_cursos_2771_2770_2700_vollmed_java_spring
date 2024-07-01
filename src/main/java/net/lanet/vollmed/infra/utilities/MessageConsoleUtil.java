package net.lanet.vollmed.infra.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConsoleUtil {
    @Autowired
    private ApplicationProperties ap;

    public void showMessage(String message) {
        if (Boolean.parseBoolean(ap.apiDebug)) {
            String showMessage = null;
            if (ap.apiDebugType.trim().equalsIgnoreCase("full")) {
                showMessage = message;
            }
            if (ap.apiDebugType.trim().equalsIgnoreCase("basic")) {
                if (ap.apiProfile.trim().equalsIgnoreCase("dev") ||
                        ap.apiProfile.trim().equalsIgnoreCase("test")) {
                    showMessage = message;
                }
            }
            if (showMessage != null) { System.out.println(showMessage); }
        }
    }
}
