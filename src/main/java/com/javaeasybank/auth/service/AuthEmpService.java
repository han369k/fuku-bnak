package com.javaeasybank.auth.service;

import com.javaeasybank.auth.repository.AuthRespository;
import java.util.List;

public interface AuthEmpService {

    // === 登入 / 登出 ===
    AuthRespository.AuthEmpResponse login(AuthRespository.LoginRequest request, String ipAddress);
    void logout(String email, String ipAddress);

    // === 員工 CRUD ===
    Long getEmpCount();
    List<AuthRespository.AuthEmpResponse> getAllEmps();
    List<AuthRespository.AuthEmpResponse> searchEmpsByName(String keyword);
    AuthRespository.AuthEmpResponse createEmp(AuthRespository.AuthEmpRequest request);
    AuthRespository.AuthEmpResponse updateEmp(String empId, AuthRespository.AuthEmpRequest request);
    void suspendEmp(String empId);
    void resumeEmp(String empId);

    // === 一鍵帶入測試資料 ===
    void seedTestData();

    // === 給其他模組對接用 ===
    AuthRespository.AuthEmpResponse getEmpByEmpId(String empId);
    AuthRespository.AuthEmpResponse getEmpByEmail(String email);
    String getRoleCodeByEmpId(String empId);
}
