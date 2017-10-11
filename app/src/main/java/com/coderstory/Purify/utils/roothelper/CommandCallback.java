package com.coderstory.Purify.utils.roothelper;

public interface CommandCallback {
    void onReadLine(String line);

    void onReadError(String line);

    void onCommandFinish();
}
