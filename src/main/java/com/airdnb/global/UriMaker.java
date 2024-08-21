package com.airdnb.global;

import java.net.URI;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class UriMaker {
    public static URI makeUri(Object id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
