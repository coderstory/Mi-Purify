package com.coderstory.Purify.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public abstract class SuHelper {

    /**
     * 执行所提交的命令组
     *
     * @return 执行结果
     */
    public final List<String> execute() throws UnsupportedEncodingException {
        ArrayList<String> commands = getCommandsToExecute();
        if (null != commands && commands.size() > 0) {
            return Shell.SU.run(commands);
        }
        return null;
    }

    protected abstract ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException;
}