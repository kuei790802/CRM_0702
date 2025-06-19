package com.example.demo.service;

import com.example.demo.dto.request.UpdateUserProfileRequest;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.Authority;
import com.example.demo.entity.CCustomer;
import com.example.demo.entity.User;
import com.example.demo.exception.AccountAlreadyExistsException;
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
    private final AuthorityRepo authorityRepo;
    private final BCryptPasswordEncoder encoder;

    // 建構自注入userRepo、encoder
    public UserService(UserRepo userRepo, AuthorityRepo authorityRepo) {
        this.userRepo = userRepo;
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



        // 以下這段應該可以刪掉?
//        boolean hasAdmin = persistedAuthorities.stream()
//                .anyMatch(auth -> auth.getCode().equalsIgnoreCase("USER_CREATE"));
//        if (!hasAdmin) {
//            throw new AccessDeniedException("沒有建立使用者的權限");
//        }


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
        //你之前有建議我要下面這段，功能是什麼，還需要嗎?
        newUser.setAuthorities(persistedAuthorities);

        return userRepo.save(newUser);
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
    public void updateOwnProfile(String account, UpdateUserProfileRequest request) {
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

        userRepo.save(user);
    }


    // 不同權限進入不同模組，如何管理權限?

    // admin查閱所有user, findbyaccount, findbyauthorities?
    // 更動權限、更動激活時間、使用者忘記密碼，可通知admin，強制重設為一次姓密碼

    // admin, 小編(cms agent?)進行前台內容管理，刪除下嫁文章、跑馬燈、輪播圖、管理活動等

    // admin, 小編(cms agent?)與ccustomer進行互動，回覆留言，更改訂單等等

    // admin調閱log

}
