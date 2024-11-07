package cn.albert.grouphelper.example;

import cn.albert.grouphelper.CheckCondition;

import java.util.Objects;

/**
 * 分班判断逻辑
 *
 * @author OuJf
 * @since v1 2024/11/7 12:03
 */
public class ScoreCheckCondition extends CheckCondition<StudentScore> {

    /**
     * 科目
     */
    private String subjects;

    /**
     * 比较符号
     */
    private String compareOperator;

    /**
     * 比较分数
     */
    private int score;

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getCompareOperator() {
        return compareOperator;
    }

    public void setCompareOperator(String compareOperator) {
        this.compareOperator = compareOperator;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean check(StudentScore studentScore) {
        int tempScore = studentScore.getScore(subjects);
        if (Objects.equals(compareOperator, "=")) {
            return tempScore == score;
        } else if (Objects.equals(compareOperator, ">")) {
            return tempScore > score;
        } else if (Objects.equals(compareOperator, ">=")) {
            return tempScore >= score;
        } else if (Objects.equals(compareOperator, "<")) {
            return tempScore < score;
        } else if (Objects.equals(compareOperator, "<=")) {
            return tempScore <= score;
        }
        return false;
    }
}
