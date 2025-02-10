package com.tugalsan.api.ftp.server;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;
import com.tugalsan.api.function.client.maythrow.checkedexceptions.TGS_FuncMTCEUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringJoiner;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class TS_FtpUtils {

    final private static TS_Log d = TS_Log.of(TS_FtpUtils.class);

    public static TGS_UnionExcuse<String> fetchWorkingDirectory(FTPClient ftpClient) {
        return TGS_FuncMTCEUtils.call(() -> {
            return TGS_UnionExcuse.of(ftpClient.printWorkingDirectory());
        }, e -> {
            return TGS_UnionExcuse.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid makeDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_FuncMTCEUtils.call(() -> {
            var result = ftpClient.makeDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid upload(FTPClient ftpClient, Path file) {
        return TGS_FuncMTCEUtils.call(() -> {
            var result = false;
            try (var is = Files.newInputStream(file)) {
                result = ftpClient.storeFile(TS_FileUtils.getNameFull(file), is);
            }
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid changeWorkingDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_FuncMTCEUtils.call(() -> {
            var result = ftpClient.changeWorkingDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuseVoid.ofVoid();
        }, e -> {
            return TGS_UnionExcuseVoid.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid destroy(FTPClient ftpClient) {
        var errors = new StringJoiner(" | ");
        TGS_FuncMTCEUtils.run(() -> ftpClient.logout(), e -> errors.add(e.toString()));
        TGS_FuncMTCEUtils.run(() -> ftpClient.disconnect(), e -> errors.add(e.toString()));
        if (!errors.toString().isEmpty()) {
            return TGS_UnionExcuseVoid.ofExcuse(d.className, "destroy", errors.toString());
        }
        return TGS_UnionExcuseVoid.ofVoid();
    }

    public static TGS_UnionExcuse<FTPClient> connect(CharSequence hostName, int port) {
        return TGS_FuncMTCEUtils.call(() -> {
            var ftpClient = new FTPClient();
            ftpClient.connect(hostName.toString(), port);
            var replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                TGS_UnionExcuse.ofExcuse(d.className, "connect", "Operation failed. Server reply code: " + replyCode);
            }
            return TGS_UnionExcuse.of(ftpClient);
        }, e -> {
            return TGS_UnionExcuse.ofExcuse(e);
        });
    }

    public static TGS_UnionExcuseVoid login(FTPClient ftpClient, CharSequence user, CharSequence pass) {
        return TGS_FuncMTCEUtils.call(() -> {
            var result = ftpClient.login(user.toString(), pass.toString());
            if (true) {
                ftpClient.enterLocalPassiveMode();
            }
            if (!result) {
                return TGS_UnionExcuseVoid.ofExcuse(d.className, "makeDirectory", "result is false");
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
