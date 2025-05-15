package io.foodapp.server.llm.prompts;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ChatPromptBuilder {
    private final ChatKnowledgeEntryRepository chatKnowledgeEntryRepository;
    private final IntentTypeRepository intentTypeRepository;
    private final MenuRepository menuRepository;
    private final VoucherRepository voucherRepository;

    public String buildIntentAndKnowledgePrompt(String question) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(
                """
                            Bạn là trợ lý AI cho khách hàng quán ăn.

                            Hãy phân loại câu hỏi sau thành một trong:

                            - FOOD (nếu hỏi về món ăn)
                            - VOUCHER (nếu hỏi về mã giảm giá)
                            - [id1, id2] (Nếu câu hỏi phù hợp với title bên dưới)
                            - Xin lỗi, tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món. (nếu câu hỏi không liên quan)

                            Dưới đây là danh sách intent và nội dung ví dụ:
                        """);

        List<IntentType> intentTypes = intentTypeRepository.findAll();
        for (IntentType type : intentTypes) {
            prompt.append("- Intent: ").append(type.getName()).append("\n");
            for (ChatKnowledgeEntry item : type.getChatKnowledgeEntrys()) {
                prompt.append("  + id: ").append(item.getId())
                        .append(", title: ").append(item.getTitle()).append("\n");
            }
        }

        prompt.append("\nCâu hỏi của khách hàng là: ").append(question).append("\n");

        prompt.append("""

                Chỉ được trả về một trong ba kết quả sau:
                - FOOD
                - VOUCHER
                - Một mảng JSON số, ví dụ: [1, 2]
                - Hoặc câu: Xin lỗi, tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món.

                Không thêm mô tả, giải thích hoặc ký tự nào khác.
                """);

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

        prompt.append(
                """
                            Hãy trả lời câu hỏi của khách hàng thân thiện.
                            Nếu không tìm thấy thông tin trả lời của khách thì trả lời: Xin lỗi, hiện tại tôi chưa có đủ thông tin để trả lời câu hỏi này. Bạn vui lòng hỏi lại theo cách khác hoặc liên hệ với nhân viên hỗ trợ nhé!
                        """);

        return prompt.toString();
    }

    public String buildFoodPrompt() {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Thực đơn của quán như sau:\n");

        List<Menu> menus = menuRepository.findAllByActiveTrue();

        for (Menu menu : menus) {
            prompt.append("- Loại: ").append(menu.getName()).append("\n");
            for (Food food : menu.getFoods()) {
                prompt.append("  + ").append(", Tên: ").append(food.getName())
                        .append(", Giá: ").append(food.getPrice())
                        .append(", Mô tả: ").append(food.getDescription())
                        .append(", Rating: ").append(food.getTotalRating())
                        .append("\n");
            }
            prompt.append("\n");
        }

        return prompt.toString();
    }

    public String buildVoucherPrompt() {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Voucher hiện có như sau:\n");

        List<Voucher> vouchers = voucherRepository.findAll().stream()
                .filter(v -> v.isPublished()
                        && (v.getStartDate() == null || !v.getStartDate().isAfter(LocalDate.now()))
                        && (v.getEndDate() == null || !v.getEndDate().isBefore(LocalDate.now())))
                .collect(Collectors.toList());

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
