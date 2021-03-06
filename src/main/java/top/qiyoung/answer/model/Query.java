package top.qiyoung.answer.model;

public class Query {
    private String search;
    private String order;
    private String type;
    private Integer role;
    private Integer index;
    private Integer size;
    private Integer id;
    private String exerciseType;


    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Query(String search, String order, String type, Integer role, Integer index, Integer size, Integer id, String exerciseType) {
        this.search = search;
        this.order = order;
        this.type = type;
        this.role = role;
        this.index = index;
        this.size = size;
        this.id = id;
        this.exerciseType = exerciseType;
    }

    public Query(String order, Integer index, Integer size) {
        this.order = order;
        this.index = index;
        this.size = size;
    }

    public Query() {
    }
}
