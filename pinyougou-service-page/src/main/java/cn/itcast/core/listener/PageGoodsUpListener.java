package cn.itcast.core.listener;

import cn.itcast.core.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PageGoodsUpListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage=(TextMessage)message;
        try {
            Long goodsId = Long.valueOf(textMessage.getText());
            staticPageService.pageDetail(goodsId);
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
