package com.dagacube.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Aspect
@Order(1) //onion layers: 2 is inside 1, 1 is inside 0.
@Slf4j
public class RestBoundaryLoggerAspect {

    private static final String MASK = "*******";

    @Value("${logging.mask.fields}")
    String[] secureFieldNames;

    //Pointcut definitions
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMapping() {//No implementation required
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {//No implementation required
    }

    @Around("restController() && (getMapping() || putMapping() || postMapping() || deleteMapping() || patchMapping() || requestMapping())")
    public Object logRestApiCall(ProceedingJoinPoint pjp) throws Throwable {
        String method = pjp.getSignature().getName();
        List<String> headers = new ArrayList<>();
        String uri = "";

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            uri = request.getRequestURI();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.add(headerName + "=" + (isSensitive(headerName) ? MASK : headerValue));
            }
        }

        String argsString = sanitizeSecureFieldValues(StringUtils.arrayToCommaDelimitedString(pjp.getArgs()));
        log.info("Inbound: [method={}, uri={}, args={}, headers={}]", method, uri, argsString, String.join(",", headers));


        long t = System.currentTimeMillis();
        Object ret = null;
        Exception ex = null;
        try {
            ret = pjp.proceed();
        } catch (Exception e) {
            ex = e;
        }
        t = System.currentTimeMillis() - t;

        if (ret instanceof ResponseEntity) {
            ResponseEntity<?> re = (ResponseEntity) ret;
            log.info("Outbound: [method={}, uri={}, duration={}, responseStatusCode={}, responseBody={}, responseHeaders={}]", method, uri, t, re.getStatusCodeValue(), re.getBody(), re.getHeaders());
        } else {
            log.info("Outbound: [method={}, uri={}, duration={}, responseObject={}]", method, uri, t, ret, ex);
        }

        if (ex != null) {
            throw ex;
        }

        return ret;
    }

    //There must be a better way of doing this, instead of depending on Lombok's toString format
    private String sanitizeSecureFieldValues(String string) {
        for (String fieldName : secureFieldNames) {

            while (true) {
                int indexOfFieldName = string.indexOf(fieldName);
                if (indexOfFieldName == -1) {
                    break;
                }
                String rightSide = string.substring(indexOfFieldName);
                int i;
                int i1 = rightSide.indexOf(", ");
                int i2 = rightSide.indexOf(")");
                if (i1 == -1) {
                    i = i2;
                } else if (i2 == -1) {
                    i = i1;
                } else {
                    i = Math.min(i1, i2);
                }

                int lengthOfFieldValue = i - fieldName.length() - 1;

                String newString = string.replace(string.substring(indexOfFieldName + fieldName.length() + 1, indexOfFieldName + fieldName.length() + 1 + lengthOfFieldValue), MASK);
                if (newString.equals(string)) {
                    break;
                }
                string = newString;
            }

        }
        return string;
    }

    private boolean isSensitive(String value) {
        return Arrays.stream(secureFieldNames).anyMatch(s -> s.equals(value.toLowerCase()));
    }

}
