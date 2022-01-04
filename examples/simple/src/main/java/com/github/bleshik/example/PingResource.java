package com.github.bleshik.example;

import jakarta.ws.rs.*;

@Path("ping")
public class PingResource {
    @GET
    public String get() {
        return "OK";
    }
}
