package cn.albert.grouphelper;

import java.util.*;

/**
 * 管理条件的添加、
 *
 * @author OuJf
 * @since v1 2024/11/6 15:57
 */
public class CheckConditionHelper<Input> {

    private final Map<String, CheckCondition<Input>> checkConditionMap = new HashMap<>();

    private CheckCondition<Input> rootCheckCondition;

    public CheckConditionHelper(CheckCondition<Input> rootCheckCondition) {
        this.rootCheckCondition = rootCheckCondition;
        parseCheckCondition(rootCheckCondition);
    }

    private void parseCheckCondition(CheckCondition<Input> checkCondition) {
        if (rootCheckCondition == null) {
            return;
        }
        String id = checkCondition.getId();
        if (checkConditionMap.containsKey(id)) {
            throw new RuntimeException("id 出现重复值");
        }
        checkConditionMap.put(id, checkCondition);
        if (checkCondition instanceof RelationCheckCondition) {
            List<CheckCondition<Input>> conditions = ((RelationCheckCondition<Input>) checkCondition).getConditions();
            if (conditions == null || conditions.isEmpty()) {
                return;
            }
            for (CheckCondition<Input> item : conditions) {
                parseCheckCondition(item);
            }
        }
    }

    public CheckCondition<Input> getRootCheckCondition() {
        if (rootCheckCondition == null) {
            return null;
        }
        return rootCheckCondition;
    }


    public void addCheckCondition(String relationConditionId, CheckCondition<Input> checkCondition) {
        checkCondition.id = createUUIDString();
        if (rootCheckCondition == null) {
            checkCondition.setParentId(null);
            rootCheckCondition = checkCondition;
            checkConditionMap.put(checkCondition.getId(), checkCondition);
            return;
        }
        CheckCondition<Input> current = checkConditionMap.get(relationConditionId);
        if (current == null) {
            return;
        }
        if (current instanceof RelationCheckCondition) {
            ((RelationCheckCondition<Input>) current).addCondition(checkCondition);
            checkConditionMap.put(checkCondition.getId(), checkCondition);
        }
        throw new RuntimeException("非分组条件，不能直接执行添加条件操作");
    }

    /**
     * @return 创建唯一Id
     */
    private String createUUIDString() {
        String uuidString;
        do {
            uuidString = UUID.randomUUID().toString();
        } while (checkConditionMap.containsKey(uuidString));
        return uuidString;
    }

    /**
     * @param id             关联Id
     * @param checkCondition 判断条件
     * @param relation       与Id对应判断条件的关系
     */
    public void relationCheckCondition(String id, CheckCondition<Input> checkCondition, Relation relation) {
        checkCondition.id = createUUIDString();
        if (rootCheckCondition == null) {
            checkCondition.setParentId(null);
            rootCheckCondition = checkCondition;
            checkConditionMap.put(checkCondition.getId(), checkCondition);
            return;
        }
        CheckCondition<Input> current = checkConditionMap.get(id);
        if (current == null) {
            return;
        }
        String parentId = current.getParentId();
        RelationCheckCondition<Input> relationCheckCondition = new RelationCheckCondition<>(relation);
        relationCheckCondition.id = createUUIDString();
        relationCheckCondition.setParentId(parentId);
        relationCheckCondition.addCondition(current);
        relationCheckCondition.addCondition(checkCondition);

        if (Objects.equals(rootCheckCondition.getId(), id)) {
            rootCheckCondition = relationCheckCondition;
        } else {
            RelationCheckCondition<Input> parent = getRelationCheckCondition(parentId);
            if (parent == null) {
                return;
            }
            parent.addCondition(relationCheckCondition);
        }
        checkConditionMap.put(relationCheckCondition.getId(), relationCheckCondition);
    }

    /**
     * @param id 删除条件
     */
    public void remove(String id) {
        if (rootCheckCondition == null) {
            return;
        }
        // 跟节点
        String rootId = rootCheckCondition.getId();
        if (Objects.equals(id, rootId)) {
            rootCheckCondition = null;
            checkConditionMap.clear();
            return;
        }
        CheckCondition<Input> current = checkConditionMap.get(id);
        RelationCheckCondition<Input> parent = getRelationCheckCondition(current.getParentId());
        if (parent == null) {
            return;
        }
        boolean success = parent.removeCondition(id);
        if (!success) {
            return;
        }
        checkConditionMap.remove(id);
        if (parent.getConditions().size() > 1) {
            return;
        }

        // 删除父节点
        CheckCondition<Input> brother = parent.getConditions().get(0);
        if (Objects.equals(parent.getId(), rootId)) {
            rootCheckCondition = brother;
        } else {
            RelationCheckCondition<Input> grandParent = getRelationCheckCondition(parent.getParentId());
            if (grandParent != null) {
                grandParent.removeCondition(parent.getId());
                checkConditionMap.remove(parent.getId());
                grandParent.addCondition(brother);
            }
        }
    }

    /**
     * @param id 判断条件Id
     * @return 返回判断条件
     */
    public CheckCondition<Input> getCheckCondition(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        return checkConditionMap.get(id);
    }


    private RelationCheckCondition<Input> getRelationCheckCondition(String id) {
        CheckCondition<Input> temp = getCheckCondition(id);
        if (temp instanceof RelationCheckCondition) {
            return (RelationCheckCondition<Input>) temp;
        }
        return null;
    }
}
