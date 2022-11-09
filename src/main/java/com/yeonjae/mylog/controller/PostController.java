package com.yeonjae.mylog.controller;

import com.yeonjae.mylog.request.PostCreate;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    // Http Method
    // GET POST PUT PATCH DELETE OPTIONS HEAD TRACE CONNECT
    // 글 등록
    // POST METHOD

    @PostMapping("/v1/posts")
    public String postV1(@RequestBody PostCreate params) throws Exception {
        // 데이터를 검증하는 이유
        // 1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
        // 2. client bug로 값이 누락될 수 있다.
        // 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해

        // 11이상부터 var 사용 가능
        //var title = params.getTitle();
        String title = params.getTitle();

        if(title == null || title.equals("")) {
            // 1. 노가다
            // 2. 개발팁: 3번 이상 반복 작업을 할때 이상함을 한번 생각해보자
            // 3. 누락가능성
            // 4. 생각보다 검증해야할 게 많다
            // 5. 개발자스럽지 않다 (노간지)
            throw new Exception("타이틀값은 필수값입니다.");
        }


        log.info("params={}", params.toString());
        return "Hello world";
    }

    // spring이 controller로 넘어오기전에 validation 체크를 한다
    // 만약 클라이언트분들께 메시지를 전달하고 싶으면 BindingResult를 추가해준다.
    @PostMapping("/v2/posts")
    public Map<String, String> postV2(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField();
            String messageError = firstFieldError.getDefaultMessage();

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, messageError);
            return error;
        }
        return Map.of();
    }
}
