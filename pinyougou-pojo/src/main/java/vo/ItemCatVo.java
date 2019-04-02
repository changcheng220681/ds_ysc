package vo;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;
import java.util.Objects;
//根据父类id - 0 ，查询分类信息
// List<ItemCat> selectByParentId(@Param("id") Long id);
public class ItemCatVo {
    private ItemCat itemCat;

    private Integer status;

    private List<ItemCat> itemCats;

    public ItemCat getItemCat() {
        return itemCat;
    }

    public void setItemCat(ItemCat itemCat) {
        this.itemCat = itemCat;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ItemCat> getItemCats() {
        return itemCats;
    }

    public void setItemCats(List<ItemCat> itemCats) {
        this.itemCats = itemCats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCatVo itemCatVo = (ItemCatVo) o;
        return Objects.equals(itemCat, itemCatVo.itemCat) &&
                Objects.equals(status, itemCatVo.status) &&
                Objects.equals(itemCats, itemCatVo.itemCats);
    }

    @Override
    public int hashCode() {

        return Objects.hash(itemCat, status, itemCats);
    }
}
