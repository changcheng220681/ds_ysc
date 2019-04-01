package cn.itcast.core.pojo.specification;

import java.io.Serializable;
import java.util.Objects;

public class Specification implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String specName;
    /**
     * 状态
     */
    private String auditStatus;

    @Override
    public String toString() {
        return "Specification{" +
                "id=" + id +
                ", specName='" + specName + '\'' +
                ", auditStatus='" + auditStatus + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specification that = (Specification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(specName, that.specName) &&
                Objects.equals(auditStatus, that.auditStatus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, specName, auditStatus);
    }

    public String getAuditStatus() {

        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

}