package com.xjtu.dormitory.user.scorer;

public class ScoreTemp {
    private int pageNum;
    private int pageSize;
    private String dorm_id;

    public int getSr_weekno() {
        return sr_weekno;
    }

    public void setSr_weekno(int sr_weekno) {
        this.sr_weekno = sr_weekno;
    }

    private int sr_weekno;
    private int sr_score;
    private String sr_remark;
    private String scorer_id;

    public String getDorm_id() {
        return dorm_id;
    }

    public void setDorm_id(String dorm_id) {
        this.dorm_id = dorm_id;
    }

    public int getSr_score() {
        return sr_score;
    }

    public void setSr_score(int sr_score) {
        this.sr_score = sr_score;
    }

    public String getSr_remark() {
        return sr_remark;
    }

    public void setSr_remark(String sr_remark) {
        this.sr_remark = sr_remark;
    }

    public String getScorer_id() {
        return scorer_id;
    }

    public void setScorer_id(String scorer_id) {
        this.scorer_id = scorer_id;
    }
}
