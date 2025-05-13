package io.foodapp.server.llm.prompts;

import java.util.List;

import org.springframework.stereotype.Component;

import io.foodapp.server.models.AiModel.ChatKnowledgeEntry;
import io.foodapp.server.models.AiModel.IntentType;
import io.foodapp.server.repositories.Ai.ChatKnowledgeEntryRepository;
import io.foodapp.server.repositories.Ai.IntentTypeRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ChatPromptBuilder {
    private final ChatKnowledgeEntryRepository chatKnowledgeEntryRepository;
    private final IntentTypeRepository intentTypeRepository;

    public String buildIntentAndKnowledgePrompt(String question) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                Bạn là một trợ lý AI chat box hỗ trợ quán ăn.

                Bạn chỉ trả lời các câu hỏi liên quan đến:
                - Quản lý quán ăn (món ăn, đặt món, khuyến mãi, hỗ trợ app,...)
                Nếu không tìm thấy thông tin phù hợp hoặc câu hỏi nằm ngoài phạm vi hỗ trợ, hãy trả lời:
                Xin lỗi, tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món.

                Dưới đây là danh sách intent type và các content mẫu:
            """);

        List<IntentType> intentTypes = intentTypeRepository.findAll();
        for (IntentType type : intentTypes) {
            prompt.append("- Intent: ").append(type.getName()).append("\n");
            for (ChatKnowledgeEntry item : type.getChatKnowledgeEntrys()) {
                prompt.append("  + ").append("id: ").append(item.getId())
                    .append(", title: ").append(item.getTitle()).append("\n");
            }
        }

        prompt.append("Câu hỏi của khách hàng: ").append(question).append("\n");
        prompt.append("""
                Yêu cầu:
                - Trả về một mảng JSON id của các ChatKnowledgeEntry phù hợp để trả lời
                - Ví dụ trả về 1,2 thì kết quả là: **[1, 2]**
                - Nếu không có gì phù hợp, trả về mảng rỗng: []
                - Không giải thích và ghi thêm bất cứ thứ gì
                """);

        return prompt.toString();
    }

    public String buildContentPrompt(List<Long> chatKnowledgeEntryIds, String question) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Bạn là trợ lý AI chat box quản lý quán ăn.\n");
        prompt.append("Các **chatKnowledgeEntry** đã được lọc phù hợp với câu hỏi của khách như sau:\n");

        List<ChatKnowledgeEntry> entries = chatKnowledgeEntryRepository.findAllById(chatKnowledgeEntryIds);
        for (ChatKnowledgeEntry entry : entries) {
            prompt.append("- ").append("title: ").append(entry.getTitle())
                    .append(", content: ").append(entry.getContent()).append("\n");
        }

        prompt.append(
                """
                    Hãy trả lời câu hỏi của khách hàng thân thiện.
                    Nếu không tìm thấy thông tin trả lời của khách thì trả lời: ❝ Xin lỗi, hiện tại tôi chưa có đủ thông tin để trả lời câu hỏi này. Bạn vui lòng hỏi lại theo cách khác hoặc liên hệ với nhân viên hỗ trợ nhé! ❞
                """);

        return prompt.toString();
    }
}

// public String buildDetectIntentType(ChatMessage question) {
// StringBuilder prompt = new StringBuilder();

// prompt.append("Bạn là trợ lý AI chat box quản lý quán ăn.\n");
// prompt.append("Trong database có các loại **intent type** như sau:\n");
// List<IntentType> intentTypes = intentTypeRepository.findAll();
// for (IntentType type : intentTypes) {
// prompt.append("- ").append("id: ").append(type.getId())
// .append(", name: ").append(type.getName()).append("\n");
// }

// prompt.append("Khách hàng hỏi: ").append(question).append("\n");
// prompt.append("""
// Từ câu hỏi của khách hàng, hãy xác định các loại **intent type** mà bạn nghĩ
// câu hỏi thuộc loại đó
// Yêu cầu:
// - Trả về đúng một mảng JSON gồm **id intent type**, định dạng như sau (ví dụ
// bạn chọn 2 intent type):
// `[12, 5]`
// """);

// return prompt.toString();
// }

// public String buildDetectContent(List<Integer> intentTypeIds, String
// question){
// StringBuilder prompt = new StringBuilder();

// prompt.append("Bạn là trợ lý AI chat box quản lý quán ăn.\n");
// prompt.append("Các **intent type** đã được lọc phù hợp với câu hỏi của khách
// có các ChatKnowledEntry sau:\n");
// List<IntentType> intentTypes =
// intentTypeRepository.findAllById(intentTypeIds);
// for (IntentType type : intentTypes) {
// prompt.append("- ").append("Intent type ").append(type.getName()).append("
// có:\n");
// for (ChatKnowledgeEntry item : type.getChatKnowledgeEntrys()) {
// prompt.append("-- id: ").append(item.getId())
// .append(", title: ").append(item.getTitle());
// }
// }

// prompt.append("Khách hàng hỏi: ").append(question).append("\n");
// prompt.append("""
// Hãy chọn ChatKnowledEntry phù hợp với câu hỏi của khách hàng để trả lời
// Yêu cầu:
// - Trả về đúng một mảng JSON gồm **id của ChatKnowledEntry**, định dạng như
// sau (ví dụ bạn chọn 2 intent type):
// `[12, 5]`
// """);

// return prompt.toString();
// }