package by.itacademy.telegram.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {


    private String botUsername = "SimbaFinanceAssistantBot";

    private String botToken = "5495997505:AAHFntyPnCcADGWUPUe9V7G8_LVHcFQ6Aeg";

    @Override
    public void onUpdateReceived(Update update) {
        try {
            Message message = update.getMessage();
            SendMessage response = new SendMessage();

            Long chatId = message.getChatId();
            response.setChatId(Long.toString(chatId));
            String text = message.getText();
            response.setText(text);

            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
