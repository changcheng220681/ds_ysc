package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillgoods")
public class SeckillGoodsController {
    @Reference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 秒杀审核搜索分页
     * @param page
     * @param rows
     * @param seckillGoods
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows,
                             @RequestBody SeckillGoods seckillGoods){
        return seckillGoodsService.search(page,rows,seckillGoods);
    }
    //开始审核  （审核通过 驳回）
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids ,String status){
        try {
            seckillGoodsService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
}
