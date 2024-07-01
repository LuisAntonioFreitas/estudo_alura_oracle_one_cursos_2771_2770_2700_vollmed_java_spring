package net.lanet.vollmed.domain.sysstatus;

import net.lanet.vollmed.domain.sysdb.ISysDbService;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import net.lanet.vollmed.infra.utilities.DateTimeUtil;
import net.lanet.vollmed.infra.utilities.TestIpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class SysStatusService implements ISysStatusService {
    @Autowired
    private ApplicationProperties ap;
    @Autowired
    private DateTimeUtil dtu;
    @Autowired
    private RestartEndpoint restartEndpoint;

    @Autowired
    private ISysDbService service;


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getStatus() {
        // Status
        final String idProfileActive = ap.profileActive.equals(ap.apiProfile)
                ? ap.profileActive : ap.profileActive + "|" + ap.apiProfile;

        ap.verifyConnectedDb = false;
        LocalDateTime databaseCreate = null;
        LocalDateTime tableCreate = null;
        try {
            databaseCreate = service.findDateFirstTableCreated(ap.databaseName);
            tableCreate = databaseCreate;

            final String catalog = databaseCreate.toString();
            ap.verifyConnectedDb = (!catalog.trim().equals("") ? true : false);

            databaseCreate = dtu.convertUTCtoLTD(databaseCreate);
            tableCreate = dtu.convertUTCtoLTD(tableCreate);
        } catch(Exception ignored) {};

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("date", dtu.getNowFormatted(DateTimeUtil.formatter));
        map.put("reference", ap.apiSystemReference);
        map.put("system", ap.apiSystemTagBase);
        map.put("version", ap.apiSystemVersion);
        map.put("status", "up");
        map.put("health", ap.verifyConnectedDb ? "up" : "down");
        map.put("type", "java");
        map.put("language", ap.apiConfigLanguage);
        map.put("server", idProfileActive);
        map.put("port", ap.serverPort);
        map.put("port-exposed", ap.serverPortExposed);
        map.put("url", ap.apiUrlBase);
        map.put("db-type", ap.databaseType);
        map.put("db-name", ap.databaseName);
        map.put("db-create", databaseCreate.toString());
//        map.put("tb-create", tableCreate.toString());
        map.put("db-connected", ap.verifyConnectedDb);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Override
    public ResponseEntity<Object> getConnectIpDb() {
        String host = ap.databaseIp;
        Object[] result = TestIpUtil.testIp(host);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("date", dtu.getNowFormatted(DateTimeUtil.formatter));
        map.put("db", ap.databaseName);
        map.put("result", result[0]);
        map.put("message", "O banco " + ap.databaseName + " foi encontrado? " + result[0]);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Override
    public ResponseEntity<Object> getConnectIp(String ip) {
        Object[] result = TestIpUtil.testIp(ip);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("date", dtu.getNowFormatted(DateTimeUtil.formatter));
        map.put("ip", ip);
        map.put("result", result[0]);
        map.put("message", result[1]);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Override
    public String getRestart() {
        System.out.println("\nRestarting the application in 3 seconds!");
        try {
            Thread.sleep(3000L);
        } catch (Exception e) {}
        System.out.println("\nRestarting the application, wait...\n");

        String result = String.format("<html lang='pt-BR'>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<title>" +
                        ap.apiSystemName +
                        "</title>" +
                        "<style type='text/css'>*{" +
                        "margin:0;padding:0;box-sizing:border-box;" +
                        "font-family:'Poppins',sans-serif,arial;color:#fff;font-size:1rem;background:#000;" +
                        "}body{margin:20px;display:inline-block;" +
                        "}</style>" +
                        "</head>" +
                        "<body>%s</body></html>", ap.apiSystemName +
                        "<br><br>Restarting the application, wait..." +
                        "<br><br>After waiting, click" +
                        "<br><a href='" + ap.apiUrlBase + "' target='_self'>" + ap.apiUrlBase + "</a>"
        );

        restartEndpoint.restart();
        return result;
    }
}
