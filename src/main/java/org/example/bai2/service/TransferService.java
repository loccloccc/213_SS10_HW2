package org.example.bai2.service;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final LangfuseClient langfuseClient;
    private final PiiMaskingService piiMaskingService;

    public void processTransfer(
            String user,
            String toAccount,
            double amount) {

        // Trong hệ thống thực tế nên nhận từ SecurityContext
        // hoặc request context thay vì tạo ngẫu nhiên.
        String sessionId = UUID.randomUUID().toString();

        // Không gửi PII gốc lên Langfuse
        String safeUserId =
                piiMaskingService.maskUserId(user);

        String maskedAccount =
                piiMaskingService.maskAccount(toAccount);

        // Chỉ gửi dữ liệu telemetry cần thiết
        Map<String, Object> safeInput = Map.of(
                "transferType", "BANK_TRANSFER",
                "recipientAccount", maskedAccount,
                "amountRange", getAmountRange(amount)
        );

        Trace trace = langfuseClient.trace(
                new Trace()
                        .name("bank-transfer")
                        .sessionId(sessionId)
                        .userId(safeUserId)
                        .input(safeInput)
        );

        try {

            log.info(
                    "Processing bank transfer. sessionId={}",
                    sessionId
            );

            // =============================
            // BUSINESS LOGIC
            // =============================

            // Không đưa số tài khoản/tên khách hàng gốc
            // vào telemetry output.
            trace.output(
                    Map.of(
                            "status", "SUCCESS",
                            "amountRange", getAmountRange(amount)
                    )
            );

        } catch (Exception e) {

            trace.output(
                    Map.of(
                            "status", "FAILED",
                            "errorType",
                            e.getClass().getSimpleName()
                    )
            );

            log.error(
                    "Bank transfer failed. sessionId={}",
                    sessionId,
                    e
            );

            throw e;
        }
    }

    private String getAmountRange(double amount) {

        if (amount < 1_000_000) {
            return "LESS_THAN_1M";
        }

        if (amount < 10_000_000) {
            return "1M_TO_10M";
        }

        return "GREATER_THAN_10M";
    }
}
