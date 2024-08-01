package com.openbook.openbook.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // UNAUTHORIZED & FORBIDDEN
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 존재하지 않습니다."),

    // BAD REQUEST
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),

    // CONFLICT
    ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 요청입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INACCESSIBLE_PERIOD(HttpStatus.CONFLICT, "접근 가능한 기간이 아닙니다."),
    ALREADY_RESERVED_AREA(HttpStatus.CONFLICT, "선택 가능한 구역이 아닙니다."),
    INVALID_DATE_RANGE(HttpStatus.CONFLICT, "입력된 날짜 범위가 유효하지 않습니다."),
    INVALID_LAYOUT_ENTRY(HttpStatus.CONFLICT, "입력된 배치도 형식에 오류가 있습니다."),
    EMPTY_TAG_DATA(HttpStatus.CONFLICT, "공백을 태그로 사용할 수 없습니다."),
    ALREADY_TAG_DATA(HttpStatus.CONFLICT, "중복 되는 태그 데이터가 있습니다."),

    // NOT FOUND
    SEARCH_RESULTS_NOT_FOUND(HttpStatus.NOT_FOUND, "검색 결과가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "행사 정보를 찾을 수 없습니다."),
    BOOTH_NOT_FOUND(HttpStatus.NOT_FOUND, "부스 정보를 찾을 수 없습니다."),
    AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "구역 정보를 찾을 수 없습니다."),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "알림 정보를 찾을 수 없습니다."),

    // INTERNET SERVER ERROR
    FIlE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
