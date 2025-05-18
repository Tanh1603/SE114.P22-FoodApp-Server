package io.foodapp.server.llm.prompts;

import java.util.List;

import org.springframework.stereotype.Component;

import io.foodapp.server.models.AiModel.ChatKnowledgeEntry;
import io.foodapp.server.models.AiModel.IntentType;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.models.MenuModel.Menu;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.repositories.Ai.ChatKnowledgeEntryRepository;
import io.foodapp.server.repositories.Ai.IntentTypeRepository;
import io.foodapp.server.repositories.Menu.MenuRepository;
import io.foodapp.server.repositories.User.VoucherRepository;
import io.foodapp.server.utils.AuthUtils;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ChatPromptBuilder {
    private final ChatKnowledgeEntryRepository chatKnowledgeEntryRepository;
    private final IntentTypeRepository intentTypeRepository;
    private final MenuRepository menuRepository;
    private final VoucherRepository voucherRepository;

    public String buildContextIntentPrompt() {
        return """
                Phân loại câu hỏi của user thành một trong các loại sau:

                - FOOD: nếu hỏi về món ăn.
                - VOUCHER: nếu hỏi về mã giảm giá.
                - [id1, id2]: nếu câu hỏi liên quan đến các tiêu đề cụ thể có id tương ứng.
                - Trả lời tự nhiên nếu là câu giao tiếp thông thường (ví dụ: "Xin chào", "Cảm ơn").
                - Trả lời: "Xin lỗi, ..." nếu câu hỏi không liên quan.

                Chỉ được trả về **một** trong các dạng sau:
                - FOOD
                - VOUCHER
                - Mảng JSON số, ví dụ: [1]
                - Một câu trả lời giao tiếp tự nhiên
                - "Xin lỗi, hiện chưa có dữ liệu và tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món."
                """;
    }

    public String buildIntentPrompt() {
        StringBuilder prompt = new StringBuilder();

        List<IntentType> intentTypes = intentTypeRepository.findAll();

        if (intentTypes.isEmpty()) {
            prompt.append("Hiện chưa có dữ liệu về kiến thức mẫu");
        } else {
            for (IntentType type : intentTypes) {
                prompt.append("- Intent: ").append(type.getName()).append("\n");
                for (ChatKnowledgeEntry item : type.getChatKnowledgeEntrys()) {
                    prompt.append("  + id: ").append(item.getId())
                            .append(", title: ").append(item.getTitle()).append("\n");
                }
            }
        }

        return prompt.toString();
    }

    public String buildContentPrompt(List<Long> chatKnowledgeEntryIds) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Các **chatKnowledgeEntry** đã được lọc phù hợp với câu hỏi của khách như sau:\n");

        List<ChatKnowledgeEntry> entries = chatKnowledgeEntryRepository.findAllById(chatKnowledgeEntryIds);
        for (ChatKnowledgeEntry entry : entries) {
            prompt.append("- ").append("title: ").append(entry.getTitle())
                    .append(", content: ").append(entry.getContent()).append("\n");
        }

        return prompt.toString();
    }

    public String buildFoodPrompt() {
        StringBuilder prompt = new StringBuilder();

        List<Menu> menus = menuRepository.findAllByActiveTrue();

        if (menus.isEmpty()) {
            prompt.append("Hiện thực đơn chưa có dữ liệu");
        } else {
            prompt.append("Thực đơn của quán như sau:\n");
        }

        for (Menu menu : menus) {
            prompt.append("- Thực đơn: ").append(menu.getName()).append("\n");
            for (Food food : menu.getFoods()) {
                prompt.append("[").append("id: ").append(food.getId())
                        .append(", Tên: ").append(food.getName())
                        .append(", Giá: ").append(food.getPrice())
                        .append(", Mô tả: ").append(food.getDescription())
                        .append(", Rating: ").append(food.getTotalRating())
                        .append("]\n");
            }
            prompt.append("\n");
        }

        return prompt.toString();
    }

    public String buildVoucherPrompt() {
        StringBuilder prompt = new StringBuilder();

        String customerId = AuthUtils.getCurrentUserUid();
        List<Voucher> vouchers = voucherRepository.findAvailableVouchersForCustomer(customerId);

        if (vouchers.isEmpty()) {
            prompt.append("Hiện chưa có khuyến mãi");
        } else {
            prompt.append("Khuyến mãi hiện có:\n");
        }

        for (Voucher voucher : vouchers) {
            prompt.append("[code: ").append(voucher.getCode())
                    .append(", value: ").append(voucher.getValue())
                    .append(", type: ").append(voucher.getType())
                    .append(", minOrderPrice: ").append(voucher.getMinOrderPrice())
                    .append(", maxValue: ").append(voucher.getMaxValue())
                    .append(", endDate: ").append(voucher.getEndDate().toString())
                    .append("]\n");
        }

        return prompt.toString();
    }
}
