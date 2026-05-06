package com.javaeasybank.auth.aspect;

import com.javaeasybank.auth.dto.AuthDto;
import com.javaeasybank.auth.entity.AuthActionLog;
import com.javaeasybank.auth.service.AuthActionLogService;
import com.javaeasybank.common.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuditAspect {

    private final AuthActionLogService actionLogService;

    public AuditAspect(AuthActionLogService actionLogService) {
        this.actionLogService = actionLogService;
    }

    @Pointcut("execution(* com.javaeasybank.auth.controller.AuthController.login(..))")
    public void loginPointcut() {}

    @Pointcut("execution(* com.javaeasybank.auth.controller.AuthController.logout(..))")
    public void logoutPointcut() {}

    @Pointcut("execution(* com.javaeasybank.auth.controller.AuthController.createEmp(..))")
    public void createEmpPointcut() {}

    @Pointcut("execution(* com.javaeasybank.auth.controller.AuthController.updateEmp(..))")
    public void updateEmpPointcut() {}

    @Pointcut("execution(* com.javaeasybank.auth.controller.AuthController.suspendEmp(..))")
    public void suspendEmpPointcut() {}

    @AfterReturning(pointcut = "loginPointcut()", returning = "result")
    public void logLogin(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            if (responseEntity.getBody() instanceof ApiResponse<?> apiResponse) {
                if (apiResponse.getData() instanceof AuthDto.AuthEmpResponse emp) {
                    saveActionLog(emp.getEmpId(), emp.getEmpName(), "LOGIN", emp.getEmpId(), "管理員登入成功");
                }
            }
        }
    }

    @AfterReturning("logoutPointcut()")
    public void logLogout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            // Since logout clears context, we might need to capture this before or handle it differently.
            // But usually, the principal is still there if we use @AfterReturning on the controller method.
            saveActionLog(auth.getName(), null, "LOGOUT", null, "管理員登出");
        }
    }

    @AfterReturning(pointcut = "createEmpPointcut()", returning = "result")
    public void logCreateEmp(JoinPoint joinPoint, Object result) {
        AuthDto.AuthEmpRequest req = (AuthDto.AuthEmpRequest) joinPoint.getArgs()[0];
        String actor = getCurrentEmpId();
        saveActionLog(actor, null, "CREATE_EMP", req.getEmpId(), "新增員工: " + req.getEmpName());
    }

    @AfterReturning(pointcut = "updateEmpPointcut()", returning = "result")
    public void logUpdateEmp(JoinPoint joinPoint, Object result) {
        String targetId = (String) joinPoint.getArgs()[0];
        AuthDto.AuthEmpRequest req = (AuthDto.AuthEmpRequest) joinPoint.getArgs()[1];
        String actor = getCurrentEmpId();
        saveActionLog(actor, null, "UPDATE_EMP", targetId, "修改員工資料: " + req.getEmpName());
    }

    @AfterReturning(pointcut = "suspendEmpPointcut()", returning = "result")
    public void logSuspendEmp(JoinPoint joinPoint, Object result) {
        String targetId = (String) joinPoint.getArgs()[0];
        String actor = getCurrentEmpId();
        saveActionLog(actor, null, "SUSPEND_EMP", targetId, "停用員工");
    }

    private void saveActionLog(String empId, String empName, String action, String target, String details) {
        AuthActionLog log = new AuthActionLog();
        log.setEmpId(empId);
        log.setEmpName(empName);
        log.setAction(action);
        log.setTarget(target);
        log.setDetails(details);
        log.setIpAddress(getClientIp());
        actionLogService.saveLog(log);
    }

    private String getCurrentEmpId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return "0.0.0.0";
    }
}
