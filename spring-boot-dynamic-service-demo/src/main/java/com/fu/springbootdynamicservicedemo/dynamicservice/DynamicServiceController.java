package com.fu.springbootdynamicservicedemo.dynamicservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DynamicServiceController {

    private final DynamicInvoker invoker;
    private final DynamicMethodRegistry registry;

    @PostMapping("/{serviceName}/{methodName}")
    public ResponseEntity<?> invokeDynamicMethod(@PathVariable String serviceName, @PathVariable String methodName, @RequestBody(required = false) String body) {
        if (!registry.hasService(serviceName)) {
            return ResponseEntity.badRequest().body("Service not found: " + serviceName);
        }
        if (!registry.hasMethod(serviceName, methodName)) {
            return ResponseEntity.badRequest().body("Method not found: " + methodName);
        }
        try {
            Object result = invoker.invoke(serviceName, methodName, body);
            return ResponseEntity.ok(result);
        } catch (Throwable e) {
            log.error("{}.{} invoke error:", serviceName, methodName, e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
