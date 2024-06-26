package net.lanet.vollmed.domain.sysstatus;

import org.springframework.http.ResponseEntity;

public interface ISysStatusService {
    ResponseEntity<Object> getStatus();
    ResponseEntity<Object> getConnectIpDb();
    ResponseEntity<Object> getConnectIp(String ip);
    String getRestart();
}
