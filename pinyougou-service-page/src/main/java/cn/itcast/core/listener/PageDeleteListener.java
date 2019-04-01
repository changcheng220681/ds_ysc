package cn.itcast.core.listener;

import cn.itcast.core.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


public class PageDeleteListener implements MessageListener{

    @Autowired
    private StaticPageService staticPageService;


    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage=(ObjectMessage)message;
        try {
            Long[] ids= (Long[]) objectMessage.getObject();
            boolean b = staticPageService.deleteItemHtml(ids);
            System.out.println("执行的结果:"+b);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
