package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.GoodsVo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;


@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference(timeout = 600000)
    private GoodsService goodsService;

    @RequestMapping("/add")
    public Result add(@RequestBody GoodsVo goodsVo){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            goodsVo.getGoods().setSellerId(username);
            goodsService.add(goodsVo);
            return new Result(true,"添加商品成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加商品失败");
        }

    }

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(username);
       return  goodsService.search(page,rows,goods);

    }

    @RequestMapping("/findOne")
    public GoodsVo findONe(Long id){
       return  goodsService.findOne(id);

    }

    @RequestMapping("/update")
    public Result update(@RequestBody GoodsVo goodsVo){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        goodsVo.getGoods().setSellerId(username);
        try {
            goodsService.update(goodsVo);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"修改成功");
        }
    }



    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination topicGoodsUpMarkTableDestination;//商品上架

    @Autowired
    private Destination topicGoodsDownMarkTableDestination;//商品下架


    @RequestMapping("/goodsIsMarkTable")
    public Result goodsIsMarkTable(Long[] ids,String status){

        if (ids == null) {
            return new Result(false,"商品ID不可以为空");
        }
        try {
            goodsService.goodsIsMarkTable(ids,status);

            for (final Long id : ids) {
                //商品上架
                if("1".equals(status)){
                        jmsTemplate.send(topicGoodsUpMarkTableDestination, new MessageCreator() {
                            @Override
                            public Message createMessage(Session session) throws JMSException {

                                return session.createTextMessage(String.valueOf(id));
                            }
                        });


                }else{//商品下架
                    jmsTemplate.send(topicGoodsDownMarkTableDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            return session.createTextMessage(String.valueOf(id));
                        }
                    });

                }

            }

            return new Result(true,"商品上下架成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"商品上下架失败");
        }
    }

}
