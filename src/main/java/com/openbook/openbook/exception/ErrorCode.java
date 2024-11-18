package com.openbook.openbook.exception;


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
    INVALID_BOOKMARK_TYPE(HttpStatus.BAD_REQUEST, "북마크 타입이 유효하지 않습니다."),
    INVALID_RESERVATION_TYPE(HttpStatus.BAD_REQUEST, "예약 타입이 유효하지 않습니다."),

    // CONFLICT
    ALREADY_PROCESSED(HttpStatus.CONFLICT, "이미 처리된 요청입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INACCESSIBLE_PERIOD(HttpStatus.CONFLICT, "접근 가능한 기간이 아닙니다."),
    UNDELETABLE_PERIOD(HttpStatus.CONFLICT, "삭제 가능한 기간이 아닙니다."),

    CANNOT_REVIEW_PERIOD(HttpStatus.CONFLICT, "리뷰 작성 가능한 기간이 아닙니다."),
    ALREADY_EXIST_REVIEW(HttpStatus.CONFLICT, "이미 작성한 리뷰가 있습니다."),

    EXCEED_MAXIMUM_IMAGE(HttpStatus.CONFLICT, "첨부할 수 있는 최대 사진 수를 초과했습니다."),
    ALREADY_RESERVED_AREA(HttpStatus.CONFLICT, "선택 가능한 구역이 아닙니다."),
    INVALID_DATE_RANGE(HttpStatus.CONFLICT, "입력된 날짜 범위가 유효하지 않습니다."),
    INVALID_LAYOUT_ENTRY(HttpStatus.CONFLICT, "입력된 배치도 형식에 오류가 있습니다."),
    INVALID_RESERVED_DATE(HttpStatus.CONFLICT, "예약 날짜가 유효하지 않습니다."),

    EMPTY_TAG_DATA(HttpStatus.CONFLICT, "공백을 태그로 사용할 수 없습니다."),
    ALREADY_TAG_DATA(HttpStatus.CONFLICT, "중복 되는 태그 데이터가 있습니다."),
    EXCEED_MAXIMUM_TAG(HttpStatus.CONFLICT, "등록할 수 있는 최대 태그 수를 초과했습니다."),

    EVENT_NOT_APPROVED(HttpStatus.CONFLICT, "승인되지 않은 행사입니다."),
    BOOTH_NOT_APPROVED(HttpStatus.CONFLICT, "승인되지 않은 부스입니다."),

    UNAVAILABLE_RESERVED_DATE(HttpStatus.CONFLICT, "예약 가능 날짜가 아닙니다."),
    UNAVAILABLE_RESERVED_TIME(HttpStatus.CONFLICT, "예약 가능 시간이 아닙니다."),
    ALREADY_RESERVED_DATE(HttpStatus.CONFLICT, "이미 존재하는 예약 날짜 입니다."),
    ALREADY_RESERVED_TIME(HttpStatus.CONFLICT, "이미 존재하는 예약 시간 입니다."),
    DUPLICATE_RESERVED_TIME(HttpStatus.CONFLICT, "중복 되는 시간 데이터가 있습니다."),
    ALREADY_RESERVED_SERVICE(HttpStatus.CONFLICT, "이미 예약된 시간 입니다."),
    RESERVATION_EXIST(HttpStatus.CONFLICT, "예약자가 존재합니다."),

    ALREADY_BOOKMARK(HttpStatus.CONFLICT, "이미 북마크 목록에 존재합니다."),

    EXCEED_MAXIMUM_CATEGORY(HttpStatus.CONFLICT, "생성할 수 있는 최대 카테고리 수를 초과했습니다."),
    DEFAULT_CATEGORY_CANNOT_DELETED(HttpStatus.CONFLICT, "기본 카테고리는 삭제할 수 없습니다."),
    ALREADY_EXIST_CATEGORY(HttpStatus.CONFLICT, "이미 존재하는 카테고리 입니다."),
    NOT_SELECTABLE_CATEGORY(HttpStatus.CONFLICT, "해당 부스의 카테고리만 선택 가능합니다."),


    // NOT FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "행사 정보를 찾을 수 없습니다."),
    EVENT_NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "행사 공지 정보를 찾을 수 없습니다."),
    BOOTH_NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "부스 공지 정보를 찾을 수 없습니다."),
    BOOTH_NOT_FOUND(HttpStatus.NOT_FOUND, "부스 정보를 찾을 수 없습니다."),
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 카테고리를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "태그 정보를 찾을 수 없습니다."),
    AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "구역 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰 정보를 찾을 수 없습니다."),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "알림 정보를 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 정보를 찾을 수 없습니다."),

    // INTERNET SERVER ERROR
    FIlE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
