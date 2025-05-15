package io.foodapp.server.llm.dto;

public class ChatMessageBuilder {

public static MistralChatMessage systemPrompt(String data) {
    StringBuilder result = new StringBuilder();
    result.append("""
        Bạn là một trợ lý AI hỗ trợ khách hàng quán ăn.
        
        Nhiệm vụ của bạn:
        - Nếu câu hỏi liên quan đến món ăn, hãy kiểm tra xem món ăn đó có trong thực đơn không.
            + Nếu có món ăn hoặc món liên quan, hãy trả lời thông tin món ăn ngắn gọn.
            + Nếu không có, hãy trả lời: "Không tìm thấy món này trong thực đơn."
        - Nếu câu hỏi không liên quan đến món ăn, nhưng nằm trong các dữ liệu được cung cấp trả lời theo yêu cầu.
        - Nếu câu hỏi nằm ngoài phạm vi hỗ trợ, hãy trả lời:
            Xin lỗi, tôi chỉ hỗ trợ các vấn đề liên quan đến quán ăn hoặc ứng dụng đặt món.

        Dưới đây là dữ liệu bạn cần xử lý:
    """);
    result.append(data);

    return new MistralChatMessage("system", result.toString());
}


    public static MistralChatMessage userPrompt(String question) {
        return new MistralChatMessage("user", question);
    }
}

