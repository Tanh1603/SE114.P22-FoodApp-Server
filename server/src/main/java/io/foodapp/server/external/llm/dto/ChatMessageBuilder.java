package io.foodapp.server.external.llm.dto;

public class ChatMessageBuilder {

    private static final String DEFAULT_CONTEXT = """
            Nhiệm vụ của bạn:
                - Phạm vi hỗ trợ của bạn là các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món.
                - Nếu dữ liệu nhận được là data trong [] thì hãy diễn giải ra thành văn bản trả lời rõ ràng
                - Nếu câu hỏi nằm ngoài phạm vi hỗ trợ, hãy trả lời:
                    Xin lỗi, tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món.
                - Nếu không có data thì trả lời là chưa có linh hoạt theo từng loại data (không được tự bịa data).
            """;

    public static MistralChatMessage systemPrompt(String context) {
        StringBuilder result = new StringBuilder();
        result.append("Bạn là một trợ lý AI hỗ trợ khách hàng quán ăn.\n");

        if (context == null || context.isBlank()) {
            result.append(DEFAULT_CONTEXT);
        } else {
            result.append(context);
        }

        return new MistralChatMessage("system", result.toString());
    }

    public static MistralChatMessage dataPrompt(String data) {
        if(data == null)
            data = "No data";
        return new MistralChatMessage("user", data);
    }

    public static MistralChatMessage userPrompt(String question) {
        if(question == null)
            question = "No question";
        return new MistralChatMessage("user", question);
    }
}
