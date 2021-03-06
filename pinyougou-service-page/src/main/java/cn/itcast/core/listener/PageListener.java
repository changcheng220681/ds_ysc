package cn.itcast.core.listener;

import cn.itcast.core.service.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService;


    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage amt=(ActiveMQTextMessage)message;
        try {
            System.out.println(amt.getText());
            Long id = Long.parseLong(amt.getText());
            System.out.println(id);
            staticPageService.pageDetail(id);

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
