package net.lanet.vollmed.infra.utilities;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class UriBuilderUtil {

    public static URI getUri(Long id, WebRequest request, UriComponentsBuilder uriBuilder) {
        String path = request.getDescription(false).replace("uri=","") + "/{id}";
        URI uri = uriBuilder.path(path).buildAndExpand(id).toUri();
        return uri;
    }
}
