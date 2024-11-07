package cn.albert.grouphelper.example;

import cn.albert.grouphelper.CheckCondition;

/**
 * 班级分班条件
 *
 * @author OuJf
 * @since v1 2024/11/7 12:02
 */
public class StudentClass {

    private String classNo;

    private CheckCondition<StudentScore> checkCondition;

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public CheckCondition<StudentScore> getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(CheckCondition<StudentScore> checkCondition) {
        this.checkCondition = checkCondition;
    }

    public boolean satisfy(StudentScore score){
        return checkCondition.check(score);
    }
}
