package cn.albert.grouphelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 判断条件与判断条件之间的关系
 *
 * @author OuJf
 * @since v1 2024/11/7 11:24
 */
public class RelationCheckCondition<Input> extends CheckCondition<Input> {
    private Relation relation;

    private List<CheckCondition<Input>> conditions;

    public RelationCheckCondition(Relation relation) {
        this.relation = relation;
        this.conditions = new ArrayList<>();
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public List<CheckCondition<Input>> getConditions() {
        return conditions;
    }

    public void setConditions(List<CheckCondition<Input>> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(CheckCondition<Input> checkCondition) {
        checkCondition.setParentId(id);
        this.conditions.add(checkCondition);
    }

    public boolean removeCondition(String id) {
        int index = -1;
        for (int i = 0; i < conditions.size(); i++) {
            CheckCondition<Input> item = conditions.get(i);
            if (Objects.equals(item.getId(), id)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }
        conditions.remove(index);
        return true;
    }

    @Override
    public boolean check(Input input) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }
        if (relation == Relation.AND) {
            for (CheckCondition<Input> item : conditions) {
                if (!item.check(input)) {
                    return false;
                }
            }
            return true;
        } else {
            for (CheckCondition<Input> item : conditions) {
                if (!item.check(input)) {
                    return true;
                }
            }
            return false;
        }
    }
}
