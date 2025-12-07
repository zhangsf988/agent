package com.zsf.agent.listener;

import org.springframework.ai.chat.listener.ChatModelListener;
import org.springframework.ai.chat.listener.ChatModelRequestContext;
import org.springframework.ai.chat.listener.ChatModelResponseContext;
import org.springframework.ai.model.function.FunctionCallingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Component;

@Component
public class ToolCallMonitor implements ChatModelListener {

    @Override
    public void onStart(ChatModelRequestContext requestContext) {
        // 在请求开始时记录
        System.out.println("=== Tool Call Request Start ===");
        System.out.println("User message: " + requestContext.getPrompt().getContents());

        // 检查是否有工具调用配置
        if (requestContext.getRequest().getOptions() instanceof FunctionCallingOptions) {
            FunctionCallingOptions options = (FunctionCallingOptions) requestContext.getRequest().getOptions();
            if (options.getFunctions() != null && !options.getFunctions().isEmpty()) {
                System.out.println("Available tools: " + options.getFunctions());
            }
        }
    }

    @Override
    public void onComplete(ChatModelResponseContext responseContext) {
        // 在响应完成时记录工具调用
        System.out.println("=== Tool Call Response ===");

        // 检查响应中是否有工具调用
        var response = responseContext.getResponse();

        if (response.getResult() != null) {
            var generation = response.getResult().getOutput();

            // 检查是否有工具调用
            if (generation.getToolCalls() != null && !generation.getToolCalls().isEmpty()) {
                System.out.println("Tool calls detected: " + generation.getToolCalls().size());

                generation.getToolCalls().forEach(toolCall -> {
                    System.out.println("Tool: " + toolCall.getFunction().getName());
                    System.out.println("Arguments: " + toolCall.getFunction().getArguments());
                    System.out.println("ID: " + toolCall.getId());
                });
            } else {
                System.out.println("No tool calls in this response");
            }
        }
    }

    @Override
    public void onError(ChatModelRequestContext requestContext, Throwable error) {
        System.err.println("Tool call error: " + error.getMessage());
    }
}
