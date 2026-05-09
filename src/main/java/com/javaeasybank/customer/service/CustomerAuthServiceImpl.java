package com.javaeasybank.customer.service;

import com.javaeasybank.common.exception.BusinessException;
import com.javaeasybank.common.util.JwtUtil;
import com.javaeasybank.customer.repository.CustomerRespository;
import com.javaeasybank.customer.entity.CustomerAuth;
import com.javaeasybank.customer.entity.CustomerProfile;
import com.javaeasybank.customer.repository.CustomerAuthRepository;
import com.javaeasybank.customer.repository.CustomerProfileRepository;
import com.javaeasybank.common.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 客戶認證服務實作：
 * 處理客戶註冊（同時建立 customer_profile + customer_auth）、
 * 登入（驗證帳密 + 發放 JWT）、個人資料維護、密碼重設等。
 */
@Service
public class CustomerAuthServiceImpl implements CustomerAuthService {

    private final CustomerAuthRepository customerAuthRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    // 用於產生隨機英數
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private final SecureRandom secureRandom = new SecureRandom();

    public CustomerAuthServiceImpl(CustomerAuthRepository customerAuthRepository,
                                   CustomerProfileRepository customerProfileRepository,
                                   PasswordEncoder passwordEncoder,
                                   JwtUtil jwtUtil,
                                   EmailService emailService) {
        this.customerAuthRepository = customerAuthRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // ===========================
    // 註冊
    // ===========================
    @Override
    @Transactional
    public CustomerRespository.LoginResponse register(CustomerRespository.RegisterRequest request) {
        // 1. 檢查帳號是否重複
        if (customerAuthRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("使用者帳號已存在");
        }
        if (customerProfileRepository.findByIdNumber(request.getIdNumber()).isPresent()) {
            throw new BusinessException("身分證字號已存在");
        }

        // 2. 建立 customer_profile
        String customerId = generateAlphanumeric(8);
        String yymm = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM"));
        String cif = yymm + "-" + generateAlphanumeric(8);

        CustomerProfile profile = new CustomerProfile();
        profile.setCustomerId(customerId);
        profile.setCif(cif);
        profile.setIdNumber(request.getIdNumber());
        profile.setName(request.getName());
        profile.setBirthday(request.getBirthday());
        profile.setGender(request.getGender());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setRegisteredAddress(request.getAddress());
        profile.setCurrentAddress(request.getAddress());
        profile.setIsPep(false);
        profile.setStatus("ACTIVE");
        customerProfileRepository.save(profile);

        // 3. 建立 customer_auth
        String verificationToken = UUID.randomUUID().toString();
        CustomerAuth auth = new CustomerAuth();
        auth.setAuthId(generateAlphanumeric(10));
        auth.setCustomerId(customerId);
        auth.setUsername(request.getUsername());
        auth.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        auth.setRole("CUSTOMER");
        auth.setStatus("PENDING"); // 註冊後預設為 PENDING
        auth.setVerificationToken(verificationToken);
        customerAuthRepository.save(auth);

        // 4. 發送驗證信
        emailService.sendVerificationEmail(request.getEmail(), verificationToken);

        CustomerRespository.LoginResponse response = new CustomerRespository.LoginResponse();
        response.setCustomerId(customerId);
        response.setCif(cif);
        response.setName(request.getName());
        response.setUsername(request.getUsername());
        response.setRole("CUSTOMER");
        return response;
    }

    // ===========================
    // 登入
    // ===========================
    @Override
    public CustomerRespository.LoginResponse login(CustomerRespository.LoginRequest request) {
        // 1. 查帳號
        CustomerAuth auth = customerAuthRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (auth == null) {
            throw new BusinessException("帳號或密碼錯誤");
        }

        // 取得客戶信箱用於發信
        CustomerProfile profile = customerProfileRepository.findById(auth.getCustomerId())
                .orElse(null);
        String email = (profile != null) ? profile.getEmail() : null;

        // 2. 檢查狀態
        if ("PENDING".equals(auth.getStatus())) {
            throw new BusinessException("帳號尚未驗證，請先至信箱收取驗證信");
        }
        if (!"ACTIVE".equals(auth.getStatus())) {
            throw new BusinessException("此帳號已被停用");
        }

        // 3. 驗密碼
        if (!passwordEncoder.matches(request.getPassword(), auth.getPasswordHash())) {
            if (email != null) {
                emailService.sendLoginNotification(email, auth.getUsername(), false, "未知位置");
            }
            throw new BusinessException("帳號或密碼錯誤");
        }

        // 4. 更新最後登入時間
        auth.setLastLoginDate(LocalDateTime.now());
        customerAuthRepository.save(auth);

        // 5. 驗證身分證字號
        if (request.getIdNumber() != null && !request.getIdNumber().isEmpty()) {
            if (profile == null || !profile.getIdNumber().equals(request.getIdNumber())) {
                if (email != null) {
                    emailService.sendLoginNotification(email, auth.getUsername(), false, "未知位置");
                }
                throw new BusinessException("身分證字號不正確");
            }
        }

        // 6. 產生 JWT
        String token = jwtUtil.generateToken(auth.getUsername(), "CUSTOMER", auth.getCustomerId());

        // 7. 發送登入成功通知
        if (email != null) {
            emailService.sendLoginNotification(email, auth.getUsername(), true, "未知位置");
        }

        CustomerRespository.LoginResponse response = new CustomerRespository.LoginResponse();
        response.setToken(token);
        response.setCustomerId(auth.getCustomerId());
        response.setCif(profile != null ? profile.getCif() : "");
        response.setName(profile != null ? profile.getName() : "");
        response.setUsername(auth.getUsername());
        response.setAvatarUrl(profile != null ? profile.getAvatarUrl() : null);
        response.setRole("CUSTOMER");
        return response;
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        CustomerAuth auth = customerAuthRepository.findByVerificationToken(token)
                .orElseThrow(() -> new BusinessException("無效的驗證連結"));

        auth.setStatus("ACTIVE");
        auth.setVerificationToken(null);
        customerAuthRepository.save(auth);
    }

    // ===========================
    // 取得個人資料
    // ===========================
    @Override
    public CustomerRespository.CustomerResponse getProfile(String customerId) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));
        CustomerAuth auth = customerAuthRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("認證資料異常"));

        return convertToResponse(profile, auth);
    }

    // ===========================
    // 修改個人資料
    // ===========================
    @Override
    @Transactional
    public CustomerRespository.CustomerResponse updateProfile(String customerId, CustomerRespository.ProfileUpdateRequest request) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));

        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getEmail() != null) profile.setEmail(request.getEmail());
        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
            profile.setCurrentAddress(request.getAddress());
        }

        customerProfileRepository.save(profile);

        CustomerAuth auth = customerAuthRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("認證資料異常"));
        return convertToResponse(profile, auth);
    }

    // ===========================
    // 上傳大頭照
    // ===========================
    @Override
    @Transactional
    public CustomerRespository.CustomerResponse uploadAvatar(String customerId, String avatarUrl) {
        CustomerProfile profile = customerProfileRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("查無此客戶"));

        profile.setAvatarUrl(avatarUrl);
        customerProfileRepository.save(profile);

        CustomerAuth auth = customerAuthRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("認證資料異常"));
        return convertToResponse(profile, auth);
    }

    // ===========================
    // 請求密碼重設（產生 token + 印出連結）
    // ===========================
    @Override
    @Transactional
    public void requestPasswordReset(CustomerRespository.PasswordResetEmailRequest request) {
        // 用 email, idNumber, birthday 找到 customer_profile
        CustomerProfile profile = customerProfileRepository.findAll().stream()
                .filter(p -> request.getEmail().equals(p.getEmail())
                        && request.getIdNumber().equals(p.getIdNumber())
                        && request.getBirthday().equals(p.getBirthday()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("驗證失敗：查無此身分資料或信箱錯誤"));

        CustomerAuth auth = customerAuthRepository.findByCustomerId(profile.getCustomerId())
                .orElseThrow(() -> new BusinessException("認證資料異常"));

        // 產生 reset token（UUID），效期 30 分鐘
        String resetToken = UUID.randomUUID().toString();
        auth.setResetToken(resetToken);
        auth.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        customerAuthRepository.save(auth);

        // 組成重設連結
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;

        emailService.sendPasswordResetEmail(request.getEmail(), resetLink);
    }

    // ===========================
    // 執行密碼重設
    // ===========================
    @Override
    @Transactional
    public void resetPassword(CustomerRespository.PasswordResetRequest request) {
        CustomerAuth auth = customerAuthRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new BusinessException("無效的重設連結"));

        if (auth.getResetTokenExpiry() == null || auth.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("重設連結已過期，請重新申請");
        }

        auth.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        auth.setResetToken(null);
        auth.setResetTokenExpiry(null);
        customerAuthRepository.save(auth);
    }

    // ===========================
    // 一鍵帶入客戶認證測試資料
    // ===========================
    @Override
    @Transactional
    public void seedAuthTestData() {
        // 為已有的 seed 客戶資料建立對應的 customer_auth
        String[][] data = {
            {"X7K9P2M4", "mingwang85"},
            {"V4L6T1Y8", "hualin90"},
            {"D3H8F5G2", "chienchen78"},
            {"B9W1C7R5", "yachang95"},
            {"P6M4N2Q8", "chihlee82"},
        };

        for (String[] d : data) {
            if (customerAuthRepository.findByCustomerId(d[0]).isEmpty()) {
                CustomerAuth auth = new CustomerAuth();
                auth.setAuthId(generateAlphanumeric(10));
                auth.setCustomerId(d[0]);
                auth.setUsername(d[1]);
                auth.setPasswordHash(passwordEncoder.encode("123456"));
                auth.setRole("CUSTOMER");
                auth.setStatus("ACTIVE");
                customerAuthRepository.save(auth);
            }
        }
    }

    // ===========================
    // 私有方法
    // ===========================
    private CustomerRespository.CustomerResponse convertToResponse(CustomerProfile profile, CustomerAuth auth) {
        CustomerRespository.CustomerResponse res = new CustomerRespository.CustomerResponse();
        org.springframework.beans.BeanUtils.copyProperties(profile, res);
        res.setUsername(auth.getUsername());
        return res;
    }

    private String generateAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_CHARS.length());
            sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
