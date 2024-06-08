package org.example.entity;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import java.util.Map;

@Value
@Builder
public class HttpRequest {
    Integer uuid;
    String content;
    @Builder.Default
    Map<String, String> headers = ImmutableMap.of();
}
