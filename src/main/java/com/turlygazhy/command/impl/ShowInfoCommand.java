package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.dao.impl.CustomerlistDao;
import com.turlygazhy.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.Map;

/**
 * Й
 * Created by user on 1/2/17.
 */
public class ShowInfoCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(ShowInfoCommand.class);
    public static final String FIRST_NAME_VAR = "firstName";
    protected CustomerlistDao customerlistDao = factory.customerlistDao();

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException {
        Long chatId = update.getMessage().getChatId();

        Message message = messageDao.getMessage(messageId);

        SendPhoto sendPhoto = message.getSendPhoto();
        SendMessage sendMessage = message.getSendMessage();
        String text = message.getSendMessage().getText();
        if (text.contains(FIRST_NAME_VAR)) {
            text = text.replaceAll(FIRST_NAME_VAR, update.getMessage().getFrom().getFirstName());
            sendMessage.setText(text);
        }
        try {
            if (sendPhoto != null) {
                bot.sendPhoto(sendPhoto.setChatId(chatId));
            }

            if (update.getMessage().getText().equals(buttonDao.getButtonText(34))) {

                for (Map<String, Object> map : customerlistDao.getList()) {
                    if (chatId.equals(map.get("chat_id"))){
                        bot.execute(sendMessage
                                .setChatId((long) map.get("chat_id"))
                                .setReplyMarkup(
                                        keyboardMarkUpDao.select(message.getKeyboardMarkUpId())
                                ).setParseMode(ParseMode.HTML)
                        );
                    }
                }
            } else {
                bot.execute(sendMessage
                        .setChatId(chatId)
                        .setReplyMarkup(
                                keyboardMarkUpDao.select(message.getKeyboardMarkUpId())
                        ).setParseMode(ParseMode.HTML)
                );
            }


            return true;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
