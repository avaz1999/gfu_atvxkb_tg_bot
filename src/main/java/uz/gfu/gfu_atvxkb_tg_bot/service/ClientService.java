package uz.gfu.gfu_atvxkb_tg_bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import uz.gfu.gfu_atvxkb_tg_bot.entitiy.BotUser;

public interface ClientService {
    void clientHasMessage(BotUser client, Message message, SendMessage sendMessage, AbsSender sender);
    void clientHasCallBackQuery(BotUser client, CallbackQuery callbackQuery,AbsSender sender);
    void stateStart(Message message,SendMessage sendMessage,BotUser client,AbsSender sender);
    void stateLastName(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void stateName(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void stateBlock(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void stateDepartment(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void stateRoomNumber(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void statePhoneNumber(Message message, SendMessage sendMessage, BotUser client,AbsSender sender);
    void stateDone(CallbackQuery callbackQuery,SendMessage sendMessage,BotUser client,AbsSender sender);
    void stateEdit(CallbackQuery callbackQuery,SendMessage sendMessage,BotUser client);
    void stateFeedback(Message message,SendMessage sendMessage,BotUser client,AbsSender sender);
    void stateSubFeedback(String data,SendMessage sendMessage,BotUser client,AbsSender sender);
}
