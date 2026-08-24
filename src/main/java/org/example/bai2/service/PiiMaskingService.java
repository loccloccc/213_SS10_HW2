package org.example.bai2.service;



import org.springframework.stereotype.Service;

@Service
public class PiiMaskingService {

    public String maskAccount(String accountNumber) {

        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }

        String lastFour =
                accountNumber.substring(accountNumber.length() - 4);

        return "****" + lastFour;
    }

    public String maskUserId(String userId) {

        if (userId == null || userId.isBlank()) {
            return "anonymous";
        }

        // Không gửi tên thật/email/số điện thoại.
        // Chỉ gửi định danh kỹ thuật đã được pseudonymized.
        return "usr_" +
                Integer.toHexString(userId.hashCode());
    }
}
