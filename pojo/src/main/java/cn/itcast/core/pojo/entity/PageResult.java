package cn.itcast.core.pojo.entity;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {
    private Long total;//总记录数
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
