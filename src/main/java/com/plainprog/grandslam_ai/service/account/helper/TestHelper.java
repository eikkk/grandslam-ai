package com.plainprog.grandslam_ai.service.account.helper;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_gen.ImageRepository;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.constant.images.ProviderId;
import com.plainprog.grandslam_ai.object.dto.auth.AccountWithPasswordDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.generation.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Component
public class TestHelper {
    public record TestAccount(Long id, String email, String password) {
    }

    //dev environment test accounts
    public static final TestAccount[] TEST_ACCOUNTS = {
            new TestAccount(53L,"test1@gslm.com", "Yn(TN@ack1nW"),
            new TestAccount(54L,"test2@gslm.com", ")*WVg4_1BXL3"),
            new TestAccount(55L,"test3@gslm.com", "ljKmLCm6#mp4"),
            new TestAccount(56L,"test4@gslm.com", "YiyYzKXoy2Mb"),
            new TestAccount(57L,"test5@gslm.com", "*VSk1Sh18lyw"),
            new TestAccount(58L,"test6@gslm.com", "1kSivKWgkf#l"),
            new TestAccount(59L,"test7@gslm.com", "M1mEV8q4#!cC"),
            new TestAccount(60L,"test8@gslm.com", "KK(LRG93COGj"),
    };

    @Value("${app.url.base}")
    private String baseUrl;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageGenerationService imageGenerationService;
    @Autowired
    private RestTemplate restTemplate;


    public AccountWithPasswordDTO ensureTestUserExists() {
        String testEmail = TEST_ACCOUNTS[0].email;
        Account account = accountService.getAccountByEmail(testEmail);
        if (account == null) {
            account = accountService.createAccount(testEmail).getAccount();
        }
        return new AccountWithPasswordDTO(account, TEST_ACCOUNTS[0].password);
    }

    public Account ensureTestUserExistAndNotVerified() {
        AccountWithPasswordDTO accountWithPass = ensureTestUserExists();
        Account account = accountWithPass.getAccount();
        AccountSecurity security = accountSecurityRepository.findByAccountId(account.getId());
        if (security == null)
            throw new RuntimeException("Account security not found for account: " + account.getId());

        if (account.getEmailVerified()) {
            account.setEmailVerified(false);
            accountRepository.save(account);
        }
        if (security.getVerifyEmailToken() != null || security.getVerifyEmailTokenCreatedAt() != null) {
            security.setVerifyEmailToken(null);
            security.setVerifyEmailTokenCreatedAt(null);
            accountSecurityRepository.save(security);
        }
        return account;
    }

    public void clearTestUser(String testEmail) {
        Account account = accountService.getAccountByEmail(testEmail);
        if (account != null) {
            accountService.deleteAccount(account.getId());
        }
    }
    public HttpHeaders initiateSession(String testEmail) {
        TestAccount account = Arrays.stream(TEST_ACCOUNTS)
                .filter(acc -> acc.email.equals(testEmail))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test account not found"));
        Account acc = accountService.getAccountByEmail(account.email);
        if (acc == null)
            throw new RuntimeException("Test account not found");
        AccountWithPasswordDTO accountWithPass = new AccountWithPasswordDTO(acc, account.password);
        return initiateSession(accountWithPass);
    }
    public HttpHeaders initiateSession() {
        AccountWithPasswordDTO accountWithPass = ensureTestUserExists();
        if (accountWithPass == null) {
            throw new RuntimeException("Test user not found");
        }
        return initiateSession(accountWithPass);
    }
    public HttpHeaders initiateSession(AccountWithPasswordDTO accountWithPass) {
        if (accountWithPass == null) {
            throw new RuntimeException("Test user not found");
        }

        ResponseEntity<String> loginResponseEntity =
                restTemplate.postForEntity(baseUrl + "/api/auth/login", new LoginRequest(accountWithPass.getAccount().getEmail(), accountWithPass.getPassword()), String.class);

        String sessionCookie = loginResponseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (sessionCookie == null) {
            throw new RuntimeException("Failed to initiate session");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", sessionCookie);
        return headers;
    }

    public Image ensureImageExists(Account account) {
        Optional<Image> testImage = imageRepository.findAllByOwnerAccountId(account.getId()).stream()
                .filter(image -> image.getNegativePrompt() != null)
                .findFirst();
        if (testImage.isPresent()) {
            return testImage.get();
        } else {
            ImgGenRequest request = new ImgGenRequest(Prompts.testPrompt, "s", ProviderId.STABLE_DIFFUSION_XL, ImgGenModuleId.RAW_STABLE_DIFFUSION_XL);
            request.setNegativePrompt("Cartoon, low resolution");
            try {
                ImgGenResponse response = imageGenerationService.generateImage(request, account, false, 0, 0);
                var id = response.getImageId();
                return imageRepository.findById(id).orElse(null);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public Account getAccount(String email){
        return accountService.getAccountByEmail(email);
    }
}