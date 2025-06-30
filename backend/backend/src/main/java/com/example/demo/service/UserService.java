package com.example.demo.service;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.UpdateUserProfileRequest;
import com.example.demo.dto.request.UserQueryRequest;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.Authority;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.exception.ForgetAccountOrPasswordException;
import com.example.demo.exception.UsernameNotFoundException;
import com.example.demo.repository.AuthorityRepo;
import com.example.demo.repository.CCustomerRepo;
import com.example.demo.repository.PasswordResetTokenRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CCustomerRepo cCustomerRepo;
    private final AuthorityRepo authorityRepo;
    private final BCryptPasswordEncoder encoder;
    private final PasswordResetTokenRepo tokenRepo;

    // 建構自注入userRepo、encoder
    public UserService(UserRepo userRepo, CCustomerRepo cCustomerRepo, AuthorityRepo authorityRepo, PasswordResetTokenRepo tokenRepo) {
        this.userRepo = userRepo;
        this.cCustomerRepo = cCustomerRepo;
        this.authorityRepo = authorityRepo;
        this.tokenRepo = tokenRepo;
        this.encoder = new BCryptPasswordEncoder(); // 或改為在外部注入
    }

    // 檢視帳號是否已存在
    public Boolean checkAccountExist(String account){
        return userRepo.findByAccount(account).isPresent();
    }

    // 密碼強度規範
    private void validatePasswordStrength(String password) {
        if (password.length() < 12) {
            throw new IllegalArgumentException("密碼長度需至少12碼");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個大寫英文字母");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個小寫英文字母");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("密碼需包含至少一個數字");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new IllegalArgumentException("密碼需包含至少一個特殊符號");
        }
    }

    // List<Authority>中有admin才能進行註冊，一次性密碼+加密、註冊時設定激活時間、操作權限
    public User register(String operatorAccount
            , String account
            , String userName
            , String password
            , String email
            , LocalDate accessEndDate
            , List<String> authorityCodes) throws AccessDeniedException {

        // 權限中有ADMIN才能增加帳戶
        User operator = userRepo.findByAccount(operatorAccount)
                .orElseThrow(() -> new UsernameNotFoundException("操作者帳號不存在"));

        boolean hasUserCreatePermission = operator.getAuthorities().stream()
                .anyMatch(auth -> auth.getCode().equalsIgnoreCase("USER_CREATE"));

        if (!hasUserCreatePermission) {
            throw new AccessDeniedException("您沒有建立使用者的權限");
        }

        //檢查新帳號是否重複
        if(checkAccountExist(account)){
            throw new AccountAlreadyExistsException(account);
        }

        //檢查新帳號密碼強度
        validatePasswordStrength(password);

        //查找實體權限，權限代碼存在於資料庫，才允許新增
        List<Authority> persistedAuthorities = authorityRepo.findByCodeIn(authorityCodes);
        if (persistedAuthorities.size() != authorityCodes.size()) {
            throw new IllegalArgumentException("權限代碼不存在於資料庫，請先建立完整權限集");
        }



        User newUser = User.builder()
                .account(account)
                .userName(userName)
                .password(encoder.encode(password))
                .email(email)
                .accessStartDate(LocalDate.now())
                .accessEndDate(accessEndDate)
                .isActive(true)
                .isDeleted(false)
                .authorities(persistedAuthorities)
                .build();

        return userRepo.save(newUser);
    }

    // 登入驗證 (JWT + OUATH2)+ 拋給別人我已經登入的資訊供後續開發
    public User login(String account, String password){
        // 測帳號 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        User loginUser = userRepo.findByAccount(account)
                .orElseThrow(() -> new ForgetAccountOrPasswordException(account, password));

        // 測密碼 //暫時，之後要改成能有重設帳密功能，跳轉介面?發送EMAIL?顯示錯誤訊息?
        String dbPassword = loginUser.getPassword();
        if(!encoder.matches(password, dbPassword)){
            throw new ForgetAccountOrPasswordException(account, password);
        }

        return loginUser;
    }


    // user檢視自己資料
    public UserProfileResponse getUserProfileByAccount(String account) {
        User user = userRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者: " + account));

        List<String> codes = user.getAuthorities().stream()
                .map(Authority::getCode)
                .collect(Collectors.toList());

        return new UserProfileResponse(
                user.getUserId(),
                user.getAccount(),
                user.getEmail(),
                user.getUserName(),
                user.isActive(),
                user.getRoleName(),
                codes,
                user.getAccessStartDate(),
                user.getAccessEndDate(),
                user.getLastLogin()
        );
    }

    // user修改自己資料
    public UserProfileResponse updateOwnProfile(String account, UpdateUserProfileRequest request) {
        User user = userRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("帳號不存在: " + account));

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("舊密碼驗證失敗");
            }
            validatePasswordStrength(request.getNewPassword());
            user.setPassword(encoder.encode(request.getNewPassword()));
        }

        User saveduser = userRepo.save(user);

        return new UserProfileResponse(
                saveduser.getUserId(),
                saveduser.getAccount(),
                saveduser.getEmail(),
                saveduser.getUserName(),
                saveduser.isActive(),
                saveduser.getRoleName(),
                saveduser.getAuthorities().stream()
                        .map(Authority::getCode)
                        .collect(Collectors.toList()),
                saveduser.getAccessStartDate(),
                saveduser.getAccessEndDate(),
                saveduser.getLastLogin()
        );
    }

    // todo: 6/30，我的前端畫面想要admin能以列表方式查閱所有user，並能在該介面進行基礎資料修正，或是點擊進入查閱詳細資訊，進行詳細資料、權限修改
    // 需要開那些api?我現有的getUserIdByAccount(String account)應該只是像是搜尋列單筆查資料，也不能針對結果進行修改吧?還是可以?
    // admin查閱所有user, findbyaccount, findbyauthorities?

    //ID找帳號，admin可以用這個方式進行資料更改嗎?
    public Long getUserIdByAccount(String account) {
        return userRepo.findByAccount(account)
                .map(User::getUserId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者: " + account));
    }


    public Page<UserProfileResponse> queryUsers(UserQueryRequest req) {
        Specification<User> spec = UserSpecification.build(req);
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), Sort.by("userId").descending());

        return userRepo.findAll(spec, pageable)
                .map(user -> new UserProfileResponse(
                        user.getUserId(),
                        user.getAccount(),
                        user.getEmail(),
                        user.getUserName(),
                        user.isActive(),
                        user.getRoleName(),
                        user.getAuthorities().stream().map(Authority::getCode).collect(Collectors.toList()),
                        user.getAccessStartDate(),
                        user.getAccessEndDate(),
                        user.getLastLogin()
                ));
    }

    //rolename找帳號?

    //authoritycode找帳號?

    //激活時間區間找帳號?

    //停權找帳號?

    //激活中找帳號?...請補充我還能幹嘛



    // todo: 6/30，請教學如果系統使用者忘記密碼，userflow通常長怎樣?這裡需要加什麼功能才會更完整?
    // 忘記密碼記性驗證gmail api?user拿到一次密碼後，自己更改密碼+加密?
    // 寄送一次性 token（不含 email 發送）
    public String generateResetToken(String email) {
        // 檢查 email 是否存在
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("查無此信箱"));

        // 清除舊的 token（可選）
        List<PasswordResetToken> oldTokens = tokenRepo.findByEmailAndUsedFalse(email);
        oldTokens.forEach(t -> t.setUsed(true));
        tokenRepo.saveAll(oldTokens);

        // 產生新 token 並儲存
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setCreatedAt(LocalDateTime.now());
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30)); // 30 分鐘有效
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        return token; // 目前回傳給前端，未來可整合 email 寄送
    }


    // 重設密碼（驗證 token、重設）
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepo.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("無效或過期的 token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token 已過期");
        }

        User user = userRepo.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("找不到帳號"));

        validatePasswordStrength(newPassword);
        user.setPassword(encoder.encode(newPassword));
        userRepo.save(user);

        // 標記 token 為已使用
        resetToken.setUsed(true);
        tokenRepo.save(resetToken);
    }


    // 更動單一使用者權限、更動激活時間、使用者忘記密碼，可通知admin，強制重設為一次姓密碼
    public UserProfileResponse updateProfileByAdmin(String account, UpdateUserProfileRequest request){
        User user = userRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("帳號不存在: " + account));

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setAccessEndDate(request.getAccessEndDate());

        List<Authority> persistedAuthorities = authorityRepo.findByCodeIn(request.getAuthorityCodes());
        if (persistedAuthorities.size() != request.getAuthorityCodes().size()){
            throw new IllegalArgumentException("部分權限代碼不存在，請確認輸入正確");
        }
        user.setAuthorities(persistedAuthorities);


        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("舊密碼驗證失敗");
            }
            validatePasswordStrength(request.getNewPassword());
            user.setPassword(encoder.encode(request.getNewPassword()));
        }

        User saveduser = userRepo.save(user);

        return new UserProfileResponse(
                saveduser.getUserId(),
                saveduser.getAccount(),
                saveduser.getEmail(),
                saveduser.getUserName(),
                saveduser.isActive(),
                saveduser.getRoleName(),
                saveduser.getAuthorities().stream()
                        .map(Authority::getCode)
                        .collect(Collectors.toList()),
                saveduser.getAccessStartDate(),
                saveduser.getAccessEndDate(),
                saveduser.getLastLogin()
        );
    }


    // 系統管理者軟刪除後臺使用者帳號
    public void disableUserAccountByAdmin(String account) {
        User user = userRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("帳號不存在"));
        user.setActive(false);
        user.setDeleted(true); // 可選
        userRepo.save(user);
    }


    //

    // 系統管理者軟刪除客戶帳戶
    public void disableCustomerAccount(String account) {
        CCustomer customer = cCustomerRepo.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("顧客帳號不存在"));
        customer.setActive(false);
        customer.setIsDeleted(true);
        cCustomerRepo.save(customer);
    }

    // admin, 小編(cms agent?)進行前台內容管理，刪除下嫁文章、跑馬燈、輪播圖、管理活動等

    // admin調閱log

}
