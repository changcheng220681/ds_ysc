package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.apache.ibatis.annotations.Many;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 收货地址管理
 */
@RestController
@RequestMapping("/address")
public class AddressController {


    @Reference
    private AddressService addressService;

    //查询收货地址
    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(name);
    }

    //查询省
    @RequestMapping("findByProvinces")
    public List<Provinces> findByProvinces() {
        return addressService.findByProvinces();
    }

    //市
    @RequestMapping("/findByCitiesId")
    public List<Cities> findByCitiesId(String parentId) {

        //Long parentId  0 查询所有一级分类
        //Long parentId  1 查询父ID为1的所有二级分页
        //Long parentId  2 查询父ID为2的所有三级分页
        return addressService.findByCitiesId(parentId);

    }

    //区
    @RequestMapping("/findByAreasId")
    public List<Areas> findByAreasId(String parentId) {

        //Long parentId  0 查询所有一级分类
        //Long parentId  1 查询父ID为1的所有二级分页
        //Long parentId  2 查询父ID为2的所有三级分页
        return addressService.findByAreasId(parentId);

    }

    //查询一个地址
    @RequestMapping("findOne")
    public Address findOne(Long id) {
        return addressService.findOne(id);
    }

    //添加
    @RequestMapping("add")
    public Result add(@RequestBody Address address) {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(name);
            addressService.add(address);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
    //添加
    @RequestMapping("update")
    public Result update(@RequestBody Address address) {
        try {
            addressService.update(address);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

}
