package org.example.entity;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class Request {
    HttpRequest httpRequest;
    @Builder.Default
    Instant timeStamp = Instant.now();
}
