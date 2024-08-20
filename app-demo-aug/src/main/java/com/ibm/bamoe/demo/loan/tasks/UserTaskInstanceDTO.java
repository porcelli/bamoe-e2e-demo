package com.ibm.bamoe.demo.loan.tasks;

public class UserTaskInstanceDTO {

    private String processInstanceId;
    private String id;
    private String name;
    private int score;
    private double income;

    // Getters and Setters

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "UserTaskInstanceDTO [processInstanceId=" + processInstanceId + ", id=" + id + ", name=" + name
                + ", score=" + score + ", income=" + income + "]";
    }

    
}
