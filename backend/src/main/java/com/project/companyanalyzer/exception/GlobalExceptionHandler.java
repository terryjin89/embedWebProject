package com.project.companyanalyzer.exception;

import com.project.companyanalyzer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * 전역 예외 처리 핸들러
 *
 * 모든 컨트롤러에서 발생하는 예외를 일관된 형식으로 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalArgumentException 처리 (잘못된 요청)
     *
     * @param ex 예외
     * @param request 웹 요청
     * @return 400 Bad Request 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request
    ) {
        log.warn("잘못된 요청 - 메시지: {}, 경로: {}", ex.getMessage(), request.getDescription(false));

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage(),
                extractPath(request)
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * RuntimeException 처리 (서버 내부 오류)
     *
     * @param ex 예외
     * @param request 웹 요청
     * @return 500 Internal Server Error 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex,
            WebRequest request
    ) {
        log.error("서버 내부 오류 발생 - 메시지: {}, 경로: {}", ex.getMessage(), request.getDescription(false), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다: " + ex.getMessage(),
                extractPath(request)
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * 모든 예외 처리 (최종 폴백)
     *
     * @param ex 예외
     * @param request 웹 요청
     * @return 500 Internal Server Error 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            WebRequest request
    ) {
        log.error("예상치 못한 오류 발생 - 메시지: {}, 경로: {}", ex.getMessage(), request.getDescription(false), ex);

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "예상치 못한 오류가 발생했습니다.",
                extractPath(request)
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * 요청 경로 추출
     *
     * @param request 웹 요청
     * @return 경로 문자열
     */
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        // "uri=/api/economy/exchange-rates" 형식에서 경로만 추출
        return description.replace("uri=", "");
    }
}
