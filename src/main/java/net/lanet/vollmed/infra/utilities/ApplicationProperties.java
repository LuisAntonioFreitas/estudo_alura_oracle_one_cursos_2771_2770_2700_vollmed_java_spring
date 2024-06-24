package net.lanet.vollmed.infra.utilities;

import net.lanet.vollmed.domain.sysdb.ISysDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ApplicationProperties {

    @Autowired
    private ISysDbService service;

    @Value("${spring.profiles.active}") public String profileActive;
    @Value("${api.profile}") public String apiProfile;

    @Value("${server.port}") public String serverPort;
    @Value("${server.port-exposed}") public String serverPortExposed;

    @Value("${spring.datasource.database-type}") public String databaseType;
    @Value("${spring.datasource.database-name}") public String databaseName;
    @Value("${spring.datasource.database-ip}") public String databaseIp;

    @Value("${api.system-tag-base}") public String apiSystemTagBase;
    @Value("${api.system-version}") public String apiSystemVersion;
    @Value("${api.system-name}") public String apiSystemName;
    @Value("${api.system-reference}") public String apiSystemReference;
    @Value("${api.system-information}") public String apiSystemInformation;
    @Value("${api.system-description}") public String apiSystemDescription;

    @Value("${api.config.path}") public String apiConfigPath;
    @Value("${api.config.language}") public String configLanguage;

    @Value("${api.url-base}") public String apiUrlBase;

    @Value("${api.cors.headers}") public String apiCorsHeaders;
    @Value("${api.cors.origins}") public String apiCorsOrigins;
    @Value("${api.cors.methods}") public String apiCorsMethods;


    @Bean
    public String status(String... result) {
        String value = result.length > 0 ? result[0] : "";

        // Status
        final String idProfileActive = this.profileActive.equals(this.apiProfile)
                ? this.profileActive : this.profileActive + "|" + this.apiProfile;

        String connectedDb = "";
        try {
            final String catalog = service.findDateFirstTableCreated(this.databaseName).toString();
            connectedDb = !catalog.trim().equals("")
                    ? String.format("Connected to the %s '%s'", this.databaseType, this.databaseName)
                    : String.format("NOT connected to the %s '%s'", this.databaseType, this.databaseName);
        } catch(Exception ex) {
            connectedDb = String.format("%s NOT connected...", this.databaseType);
        };

        if (Objects.equals(value.trim().toLowerCase(), "console".trim().toLowerCase())) {
            System.out.printf("\n%s\n", this.apiSystemName);
            System.out.printf("%s | API\n", this.apiSystemDescription);
            System.out.printf("Server '%s' ON port %s is running!\n", idProfileActive, this.serverPort);
            System.out.printf("%s/...\n", this.apiUrlBase);
            System.out.printf("%s/doc | Swagger\n", this.apiUrlBase);
            System.out.printf("%s | %s (%s)\n", connectedDb, this.apiSystemTagBase, this.configLanguage);
            return null;
        } else {
            return String.format("%s</br>\n", this.apiSystemName)
                    + String.format("%s | API</br>\n", this.apiSystemDescription)
                    + String.format("Server '%s' ON port %s is running!</br>\n", idProfileActive, this.serverPort)
                    + String.format("%s/...</br>\n", this.apiUrlBase)
                    + String.format("<a href='%s/doc' target='_blank'>%s/doc</a> | Swagger</br>\n", this.apiUrlBase, this.apiUrlBase)
                    + String.format("%s | %s (%s)</br>\n", connectedDb, this.apiSystemTagBase, this.configLanguage);

        }

    }

}
