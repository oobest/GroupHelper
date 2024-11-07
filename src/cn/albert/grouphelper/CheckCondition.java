package cn.albert.grouphelper;

/**
 * 判断条件抽象
 *
 * @author OuJf
 * @since v1 2024/11/6 15:52
 */
public abstract class CheckCondition<Input> {

    private String parentId;

    protected String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public abstract boolean check(Input input);
}
