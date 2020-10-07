import com.google.inject.spi.Dependency;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.lang.model.type.ArrayType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* Технология LongPool - Это очередь ожидающих запросов
    1. Сначала отправляется запрос на сервер (Соединение не закрывается, пока не появится новое событие)
    2. Событие отправляется в ответ на запрос.
    3. Клиент отправляет новый ожидающий запрос.
*/

public class Bot extends TelegramLongPollingBot { //Необходимо унаследовать класс Bot от TelegramLongPollingBot
    public static void main(String[] args) {      //Точка входа в объект
        ApiContextInitializer.init();             //Инициализируем объект Api
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(); //Создаём телеграм объект
        try {                                     //Необходимо зарегистрировать бота
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) { //Обработка исключений
            e.printStackTrace();                  //Имплементируем три метода
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()){
            switch (message.getText()){
                case "/start":
                    sendMsg(message,"Привет\nЯ бот Олега\n");
                    break;
                case "Help":
                     sendMsg(message,"Чем могу помочь?");
                    break;
                case "/settings":
                     sendMsg(message, "Настройки");
                    break;
                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        if (message != null && message.hasText()) {
            switch (message.getText()){
                case "Ты пидор":
                    sendMsg(message,"НЕТ ТЫ!");
                    break;
                case "a tut Tbl nudop":
                    sendMsg(message,"Sam takoy!!");
                    break;
            }
        }
    }


    public void setButtons(SendMessage sendMessage) {
        /* ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("Ты пидор"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        */

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        inlineKeyboardButton.setText("Help");
        inlineKeyboardButton.setCallbackData("Помощь уже тут");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); // Ряд кнопок
        keyboardButtonsRow1.add(inlineKeyboardButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); // Объединение ряда кнопок
        rowList.add(keyboardButtonsRow1);  // Тут бы мог быть второй ряд кнопок

        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Settings")
                .setCallbackData("Меню настройки"));

        inlineKeyboardMarkup.setKeyboard(rowList);

    }

    public String getBotUsername() {
        return "sosiolegbot";
    }

    public String getBotToken() {
        return "1013041892:AAGfE0SnBQ1XPpcaaOLSmcdX0xYcSfxbArE";
    }
}