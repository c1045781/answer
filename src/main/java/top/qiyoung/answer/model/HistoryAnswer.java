package top.qiyoung.answer.model;

public class HistoryAnswer {
    private Integer historyAnswerid;
    private String answer;
    private Integer order;
    private Integer historyId;

    public Integer getHistoryAnswerid() {
        return historyAnswerid;
    }

    public void setHistoryAnswerid(Integer historyAnswerid) {
        this.historyAnswerid = historyAnswerid;
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
