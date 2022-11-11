package com.yeonjae.mylog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    /*  Builder의 장점
        - 가독성이 좋다.
        - 필요한 값만 받을 수 있다. // -> (오버로딩 가능한 조건 찾아보세요)
        - 객체의 불변성

        @Builder는 클래스 선언과 생성자 선언이 있다.
        - 클래스 선언을 하게되면 모든 매개변수가 생성자의 파람으로 들어가게 됨으로써
          객체 생성 시 받지 않아야 할 매개변수들도 빌더에 노출이 된다.
        - 생성자 선언을 하게되면 테스트 시 Setter를 사용하게 될 수 있다.

        -> 해결안: @Builder.Default를 사용해서 값이 할당 되지 않은 경우 초기값을 설정할 수 있음
     */
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
