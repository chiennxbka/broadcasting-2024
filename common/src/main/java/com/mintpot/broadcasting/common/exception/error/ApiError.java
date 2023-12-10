package com.mintpot.broadcasting.common.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mintpot.broadcasting.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError {

	private HttpStatus status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;

	private int errorCode;
	private String message;
	private List<ApiSubError> subErrors;

	private ApiError() {
		timestamp = LocalDateTime.now();
	}

	public ApiError(HttpStatus status, int errorCode) {
		this();
		this.status = status;
		this.errorCode = errorCode;
		this.message = "Unexpected error";
	}

	public ApiError(HttpStatus status, String message, int errorCode) {
		this(status, errorCode);
		this.message = message;
	}

	public ApiError(ErrorCode errorCode) {
		this(errorCode.getHttpStatus(), errorCode.getMessage(), errorCode.getCode());
	}
}
