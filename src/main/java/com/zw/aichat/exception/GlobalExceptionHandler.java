package com.zw.aichat.exception;

import com.zw.aichat.bean.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static cn.dev33.satoken.SaManager.log;

@RestControllerAdvice
public class GlobalExceptionHandler extends  Throwable {

    /**
     * 专门处理数据库唯一约束冲突的异常（例如：用户名重复）。
     * 当尝试插入或更新数据，违反了数据库的唯一性约束时，会抛出此异常。
     * @param ex 数据库完整性违规异常
     * @return 包含“用户名已存在”的响应
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // 返回 409 Conflict 状态码，表示资源冲突，这比 429 或 500 都更符合语义
        Result<?> result = Result.error(HttpStatus.CONFLICT.value(), "用户名已存在，请换一个重试");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    /**
     * 处理其他所有未被特定处理器捕获的运行时异常。
     * 这是一个兜底的处理器，防止未知错误导致程序崩溃。
     * @param ex 运行时异常
     * @return 包含“服务器内部错误”的响应
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Result<?>> handleRuntimeException(RuntimeException ex) {
        // 在实际项目中，强烈建议在这里记录详细的错误日志，方便排查未知问题
        // 例如： log.error("发生了一个未捕获的运行时异常", ex);
        log.error("发生了一个未捕获的运行时异常", ex);
        ex.printStackTrace();
        // 返回通用的 500 服务器内部错误
        Result<?> result = Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请稍后重试");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
