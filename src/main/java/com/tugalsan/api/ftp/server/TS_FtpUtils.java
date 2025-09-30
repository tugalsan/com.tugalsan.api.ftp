package com.tugalsan.api.ftp.server;

import module com.tugalsan.api.file;
import module com.tugalsan.api.list;
import module com.tugalsan.api.log;
import module com.tugalsan.api.union;
import module com.tugalsan.api.function;
import module org.apache.commons.net;
import java.nio.file.*;
import java.util.*;

public class TS_FtpUtils {

    final private static TS_Log d = TS_Log.of(TS_FtpUtils.class);

    public static TGS_UnionExcuse<String> fetchWorkingDirectory(FTPClient ftpClient) {
        return TGS_FuncMTCUtils.call(() -> {
            return TGS_UnionExcuse.of(ftpClient.printWorkingDirectory());
        }, e -> {
            return TGS_UnionExcuse.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid makeDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_FuncMTCUtils.call(() -> {
            var result = ftpClient.makeDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className(), "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid upload(FTPClient ftpClient, Path file) {
        return TGS_FuncMTCUtils.call(() -> {
            var result = false;
            try (var is = Files.newInputStream(file)) {
                result = ftpClient.storeFile(TS_FileUtils.getNameFull(file), is);
            }
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className(), "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid changeWorkingDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_FuncMTCUtils.call(() -> {
            var result = ftpClient.changeWorkingDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className(), "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid destroy(FTPClient ftpClient) {
        var errors = new StringJoiner(" | ");
        TGS_FuncMTCUtils.run(() -> ftpClient.logout(), e -> errors.add(e.toString()));
        TGS_FuncMTCUtils.run(() -> ftpClient.disconnect(), e -> errors.add(e.toString()));
        if (!errors.toString().isEmpty()) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className(), "destroy", errors.toString());
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static TGS_UnionExcuse<FTPClient> connect(CharSequence hostName, int port) {
        return TGS_FuncMTCUtils.call(() -> {
            var ftpClient = new FTPClient();
            ftpClient.connect(hostName.toString(), port);
            var replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                TGS_UnionExcuse.ofExcuse(d.className(), "connect", "Operation failed. Server reply code: " + replyCode);
            }
            return TGS_UnionExcuse.of(ftpClient);
        }, e -> {
            return TGS_UnionExcuse.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid login(FTPClient ftpClient, CharSequence user, CharSequence pass) {
        return TGS_FuncMTCUtils.call(() -> {
            var result = ftpClient.login(user.toString(), pass.toString());
            if (true) {
                ftpClient.enterLocalPassiveMode();
            }
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className(), "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static List<String> fetchReply(FTPClient ftpClient) {
        return TGS_ListUtils.of(ftpClient.getReplyStrings());
    }

}
