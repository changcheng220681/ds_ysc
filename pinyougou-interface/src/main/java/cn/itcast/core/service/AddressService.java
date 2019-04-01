package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;

import java.util.List;

public interface AddressService {
    List<Address> findListByLoginUser(String name);

    List<Provinces> findByProvinces();

    List<Cities> findByCitiesId(String parentId);

    List<Areas> findByAreasId(String parentId);

    Address findOne(Long id);

    void add(Address address);

    void update(Address address);
}
