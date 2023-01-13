package com.project.feeely.util.auth;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class URLMatcher implements RequestMatcher {
    // RequestMatcher는 해당 필터에 대한 Url, Method 설정 부분

    private OrRequestMatcher requestMatcher;

    // Custom RequestMatcher를 만들어주는 것
    // rRequestMatcher를 이용하면 여러개의 RequestMatcher를 필터링 할 수 있다고 함..

    public URLMatcher(List<String> pathList) {
        if(!pathList.isEmpty()) {
            List<RequestMatcher> requestMatcherList = pathList.stream()
                    .map(AntPathRequestMatcher::new)
                    .collect(Collectors.toList());

             requestMatcher = new OrRequestMatcher(requestMatcherList);
        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

}
