package com.turlygazhy.command.impl;

import com.turlygazhy.Bot;
import com.turlygazhy.command.Command;
import com.turlygazhy.dao.impl.CustomerlistDao;
import com.turlygazhy.dao.impl.KeyWordDao;
import com.turlygazhy.entity.WaitingType;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomersListCommand extends Command {

    protected CustomerlistDao customerlistDao = factory.customerlistDao();

    @Override
    public boolean execute(Update update, Bot bot) throws SQLException, TelegramApiException {

        if (update.getMessage() != null && update.getMessage().getContact() == null) {
            if (update.getMessage().getText().equals(buttonDao.getButtonText(245))) {

                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> table = new ArrayList<>();
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonDao.getButtonText(246));
                button.setCallbackData("➕ Добавить клиента");


                row.add(button);
                table.add(row);
                keyboard.setKeyboard(table);


                StringBuilder sb = new StringBuilder();
                sb.append("Список ползователей которые приобрели базу участников \n\n");
                List<String> list = customerlistDao.get();
                for (String map : list) {
                    sb.append(map).append("\n");
                }

                bot.sendMessage(new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(sb.toString())
                        .enableHtml(true)
                        .setReplyMarkup(keyboard)
                );


            }
        }
        if (update.getCallbackQuery() != null) {
            if (update.getCallbackQuery().getData().equals(buttonDao.getButtonText(246))) {

                bot.sendMessage(new SendMessage()
                        .setChatId(update.getCallbackQuery().getFrom().getId().longValue())
                        .setText("Отправьте контакт покупателя")
                        .enableHtml(true)
                );
            }
        }


        if (update.getMessage().getContact() != null) {


            customerlistDao.insert(update.getMessage().getContact().getUserID(),
                    update.getMessage().getContact().getFirstName(),
                    update.getMessage().getContact().getLastName()
                    );

            bot.sendMessage(new SendMessage()
                    .setChatId((long) update.getMessage().getFrom().getId())
                    .setText("Пользователь успешно добавлен")
                    .enableHtml(true)
            );
        }


        return false;
    }
}
