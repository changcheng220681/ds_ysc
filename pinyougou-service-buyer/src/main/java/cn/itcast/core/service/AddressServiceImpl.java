package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.dao.address.AreasDao;
import cn.itcast.core.dao.address.CitiesDao;
import cn.itcast.core.dao.address.ProvincesDao;
import cn.itcast.core.pojo.address.*;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 收货地址管理
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private ProvincesDao provincesDao;
    @Autowired
    private CitiesDao citiesDao;
    @Autowired
    private AreasDao areasDao;
    @Override
    public List<Address> findListByLoginUser(String name) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name);
        return addressDao.selectByExample(addressQuery);
    }

    @Override
    public List<Provinces> findByProvinces() {
        return provincesDao.selectByExample(null);
    }

    @Override
    public List<Cities> findByCitiesId(String  parentId) {
        CitiesQuery query = new CitiesQuery();
        query.createCriteria().andProvinceidEqualTo(parentId);
        return citiesDao.selectByExample(query);
    }

    @Override
    public List<Areas> findByAreasId(String parentId) {
        AreasQuery query = new AreasQuery();
        query.createCriteria().andCityidEqualTo(parentId);
        return areasDao.selectByExample(query);
    }

    @Override
    public Address findOne(Long id) {
        return addressDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(Address address) {
        address.setIsDefault("0");
        address.setCreateDate(new Date());
        addressDao.insertSelective(address);
    }

    @Override
    public void update(Address address) {
        addressDao.updateByPrimaryKeySelective(address);
    }
    //
}
