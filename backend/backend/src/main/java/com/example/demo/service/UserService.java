package com.example.demo.service;

import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.request.UpdateUserProfileRequest;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.Authority;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.User;
import com.example.demo.exception.AccountAlreadyExistsException;
import com.example.demo.exception.ForgetAccountOrPasswordException;
import com.example.demo.exception.UsernameNotFoundException;
import com.example.demo.repository.AuthorityRepo;
import com.example.demo.repository.CCustomerRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final CCustomerRepo cCustomerRepo;
    private final AuthorityRepo authorityRepo;
    private final BCryptPasswordEncoder encoder;

    // 建構自注入userRepo、encoder
    public UserService(UserRepo userRepo, CCustomerRepo cCustomerRepo, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
        this.cCustomerRepo = cCustomerRepo;
        this.authorityRepo = authorityRepo;
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

    // user拿到admin給的一次密碼後，自己更改密碼+加密

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

    //ID找帳號
    public Long getUserIdByAccount(String account) {
        return userRepo.findByAccount(account)
                .map(User::getUserId)
                .orElseThrow(() -> new UsernameNotFoundException("找不到使用者: " + account));
    }


    // 不同權限進入不同模組，如何管理權限?

    // admin查閱所有user, findbyaccount, findbyauthorities?
    // 更動使用者權限、更動激活時間、使用者忘記密碼，可通知admin，強制重設為一次姓密碼
    // todo
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
        customer.setDeleted(true);
        cCustomerRepo.save(customer);
    }

    // admin, 小編(cms agent?)進行前台內容管理，刪除下嫁文章、跑馬燈、輪播圖、管理活動等

    // admin, 小編(cms agent?)與ccustomer進行互動，更改訂單

    // admin調閱log

}
