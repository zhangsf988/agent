package com.zsf.agent.configuration;

import com.alibaba.fastjson2.JSONObject;
import com.zsf.agent.entity.EmployeeApproval;
import com.zsf.agent.service.EmployeeApprovalService;
import io.grpc.internal.JsonUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class ToolConfig {

    @Autowired
    EmployeeApprovalService employeeApprovalService;

    @Tool(name = "当前时间",description = "获取当前时间")
    public String getCurrentTime() {
        return "The current time is " + new java.util.Date();
    }

    @Tool(description = "记录一个公司职工提交的审批流程，比如离职、申请宿舍等申请。保存职工的姓名、年龄、司龄、性别、审批事项、原因")
    public String saveEmployeeApproval(String name, int age, BigDecimal serviceYears, String gender, String approvalItem, String reason) {
        System.out.println("保存职工的审批信息：" + name + " " + age + " " + serviceYears + " " + gender + " " + approvalItem + " " + reason);
        EmployeeApproval employeeApproval = new EmployeeApproval(name, age, serviceYears, gender, approvalItem, reason);
        System.out.println(JSONObject.toJSONString(employeeApproval));
        employeeApprovalService.saveApproval(employeeApproval);
        return "已保存职工的审批信息：" + name + " " + age + " " + serviceYears + " " + gender + " " + approvalItem + " " + reason;
    }

    @Tool(description = "查询审批信息,如果没有指定姓名，就查询所有审批信息")
    public String getEmployeeApproval(String name) {

        List<EmployeeApproval> approvals = employeeApprovalService.getApprovalsByEmployeeName(name);
        if (approvals.isEmpty()) {
            return "没有找到职工的审批信息";
        }
        StringBuilder sb = new StringBuilder();
        for (EmployeeApproval approval : approvals) {
            sb.append("职工姓名：").append(approval.getEmployeeName())
              .append("，年龄：").append(approval.getAge())
              .append("，司龄：").append(approval.getCompanyAge())
              .append("，性别：").append(approval.getGender())
              .append("，审批事项：").append(approval.getApprovalType())
              .append("，原因：").append(approval.getReason())
              .append("\n");
        }
        return "职工的审批信息为：" + sb.toString();
    }

}
