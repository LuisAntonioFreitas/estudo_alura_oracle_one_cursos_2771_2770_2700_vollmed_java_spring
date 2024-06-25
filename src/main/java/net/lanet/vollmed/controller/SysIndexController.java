package net.lanet.vollmed.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.lanet.vollmed.infra.utilities.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
@CrossOrigin(allowedHeaders = "**", origins = "**", methods = {RequestMethod.GET})
@Tag(name = "Index")
@RequestMapping(path = "${api.config.path}")
public class SysIndexController {
    @Autowired
    private ApplicationProperties ap;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping(path = {""}, produces = MediaType.TEXT_HTML_VALUE)
    public String getIndex() {

        Context context = new Context();
        context.setVariable("title", ap.apiSystemName);
        context.setVariable("message", ap.status());
        String html = templateEngine.process("index", context);

        return html;
    }
}
