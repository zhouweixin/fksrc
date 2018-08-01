package com.hnu.fk.exception;

import com.hnu.fk.domain.Result;
import com.hnu.fk.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class HandleExceptions {

	private final static Logger logger = LoggerFactory.getLogger(HandleExceptions.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result<Object> handle(Exception e) {
		if (e instanceof FkExceptions) {
			// 自定义异常
			FkExceptions fkException = (FkExceptions) e;
			return ResultUtil.error(fkException);
		} else if (e instanceof MethodArgumentTypeMismatchException) {
			logger.error("【系统异常】 {}", e.getMessage());
			// 参数类型不匹配
			return ResultUtil.error(new FkExceptions(EnumExceptions.ARGU_MISMATCH_EXCEPTION));
		} else {
			logger.error("【系统异常】 {}", e.getMessage());

			if (e.getMessage().contains("Request method")) {
				// 请求方法不匹配
				return ResultUtil.error(new FkExceptions(EnumExceptions.REQUEST_METHOD));
			} else if(e.getMessage().contains("constraint [null]")){
                return ResultUtil.error(new FkExceptions(EnumExceptions.DELETE_FAILED_REF_KEY_EXISTS));
            }else {
			    // 开发阶段不显示未知异常
                EnumExceptions.UNKOWN_ERROR.setMessage(e.getMessage());
				return ResultUtil.error(new FkExceptions(EnumExceptions.UNKOWN_ERROR));
			}
		}
	}
}
