import cn.albert.grouphelper.Relation;
import cn.albert.grouphelper.RelationCheckCondition;
import cn.albert.grouphelper.example.ScoreCheckCondition;
import cn.albert.grouphelper.example.StudentClass;
import cn.albert.grouphelper.example.StudentScore;

/**
 * @author OuJf
 * @since v1 2024/11/6 15:51
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        StudentClass studentClass = new StudentClass();
        studentClass.setClassNo("一班");
        RelationCheckCondition<StudentScore> relationCheckCondition = new RelationCheckCondition<>(Relation.AND);
        ScoreCheckCondition scoreCheckCondition = new ScoreCheckCondition();
        scoreCheckCondition.setSubjects("语文");
        scoreCheckCondition.setScore(95);
        scoreCheckCondition.setCompareOperator(">=");
        relationCheckCondition.addCondition(scoreCheckCondition);
        // TODO
        studentClass.setCheckCondition(relationCheckCondition);

        // 判断学生是否可以分到次班级
        StudentScore studentScore = new StudentScore();
        studentClass.satisfy(studentScore);
    }
}