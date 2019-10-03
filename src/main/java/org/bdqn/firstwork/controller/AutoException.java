package org.bdqn.firstwork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理一些由页面spring boot自定义的异常
 * @author Administrator
 *
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class AutoException implements ErrorController{

	@Override
	public String getErrorPath() {
		return "error";
	}
	
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request,Model model) {
		HttpStatus status = getStatus(request);
		if(status.is4xxClientError()) {
			model.addAttribute("message", "你的请求出问题了，换个姿势试一下？");
		}else if(status.is5xxServerError()) {
			model.addAttribute("message", "服务器端冒烟了，请稍后再试？");
		}
		return new ModelAndView("error");
	}
	
	protected HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		}
		catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}
}
