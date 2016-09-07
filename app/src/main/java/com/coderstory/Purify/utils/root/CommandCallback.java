package com.coderstory.Purify.utils.root;

public interface CommandCallback {
    void onReadLine(String line);

    void onReadError(String line);

    void onCommandFinish();
}
