package cn.throwx.octopus.server.application.controller;

import cn.throwx.octopus.server.filter.TransformContext;
import cn.throwx.octopus.server.service.UrlMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2020/12/26 19:15
 */
@RequiredArgsConstructor
@RestController
public class OctopusController {

    private final UrlMapService urlMapService;

    @GetMapping(path = "/{compressionCode}")
    @ResponseStatus(HttpStatus.FOUND)
    public Mono<Void> dispatch(@PathVariable(name = "compressionCode") String compressionCode, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        TransformContext context = new TransformContext();
        context.setCompressionCode(compressionCode);
        context.setParam(TransformContext.PARAM_SERVER_WEB_EXCHANGE_KEY, exchange);
        if (Objects.nonNull(request.getRemoteAddress())) {
            context.setParam(TransformContext.PARAM_REMOTE_HOST_NAME_KEY, request.getRemoteAddress().getHostName());
        }
        HttpHeaders httpHeaders = request.getHeaders();
        Set<String> headerNames = httpHeaders.keySet();
        if (!CollectionUtils.isEmpty(headerNames)) {
            headerNames.forEach(headerName -> {
                String headerValue = httpHeaders.getFirst(headerName);
                context.setHeader(headerName, headerValue);
            });
        }
        // 处理转换
        urlMapService.processTransform(context);
        // 这里有一个技巧,flush用到的线程和内部逻辑处理的线程不是同一个线程,所有要用到TTL
        return Mono.fromRunnable(context.getRedirectAction());
    }

    @PostMapping(path = "/api/url")
    public String create(@RequestBody cn.throwx.octopus.server.model.entity.UrlMap body) {
        return urlMapService.createUrlMap("localhost:9099", body);
    }

    @PutMapping(path = "/api/url/{id}")
    public Long edit(@PathVariable("id") Long id, @RequestBody cn.throwx.octopus.server.model.entity.UrlMap body) {
        body.setId(id);
        return urlMapService.editUrlMap(body);
    }

    @DeleteMapping(path = "/api/url/{id}")
    public void delete(@PathVariable("id") Long id) {
        urlMapService.deleteUrlMap(id);
    }
}
