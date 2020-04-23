package com.example.myapplication;

import android.view.textclassifier.ConversationActions;

import java.io.Serializable;

public class Messages implements Serializable {
    private String messagesTitle;
    private String messagesContent;
    private String messagesDate;

    Messages() {}

    public Messages(String date, String title, String content) {
        this.messagesDate= date;
        this.messagesTitle = title;
        this.messagesContent = content;
    }

    String getMessagesDate() {
        return messagesDate;
    }

    void setMessagesDate(String date) {
        this.messagesDate = date;
    }

    String getMessagesTitle() {
        return messagesTitle;
    }

    void setMessagesTitle(String title) {
        this.messagesTitle = title;
    }

    String getMessagesContent() {
        return messagesContent;
    }

    void setMessagesContent(String content) {
        this.messagesContent = content;
    }

}
