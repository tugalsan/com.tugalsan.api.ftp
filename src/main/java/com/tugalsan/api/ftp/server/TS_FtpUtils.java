package com.tugalsan.api.ftp.server;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class TS_FtpUtils {

    final private static TS_Log d = TS_Log.of(TS_FtpUtils.class);

    public static Optional<String> fetchWorkingDirectory(FTPClient ftpClient) {
        return TGS_UnSafe.call(() -> {
            return Optional.of(ftpClient.printWorkingDirectory());
        }, e -> {
            e.printStackTrace();
            return Optional.empty();
        });
    }

    public static boolean makeDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_UnSafe.call(() -> {
            return ftpClient.makeDirectory(newDir_withSlashPrefix.toString());
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    public static boolean upload(FTPClient ftpClient, Path file) {
        return TGS_UnSafe.call(() -> {
            return ftpClient.storeFile(TS_FileUtils.getNameFull(file), Files.newInputStream(file));
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    public static boolean changeWorkingDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        return TGS_UnSafe.call(() -> {
            return ftpClient.changeWorkingDirectory(newDir_withSlashPrefix.toString());
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    public static void destroy(FTPClient ftpClient) {
        TGS_UnSafe.run(() -> ftpClient.logout(), e -> TGS_StreamUtils.runNothing());
        TGS_UnSafe.run(() -> ftpClient.disconnect(), e -> TGS_StreamUtils.runNothing());
    }

    public static Optional<FTPClient> connect(CharSequence hostName, int port) {
        return TGS_UnSafe.call(() -> {
            var ftpClient = new FTPClient();
            ftpClient.connect(hostName.toString(), port);
            var replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                TGS_UnSafe.thrw(d.className, "connect", "Operation failed. Server reply code: " + replyCode);
            }
            return Optional.of(ftpClient);
        }, e -> {
            e.printStackTrace();
            return Optional.empty();
        });
    }

    public static boolean login(FTPClient ftpClient, CharSequence user, CharSequence pass) {
        return TGS_UnSafe.call(() -> {
            var success = ftpClient.login(user.toString(), pass.toString());
            if (true) {
                ftpClient.enterLocalPassiveMode();
            }
            return success;
        }, e -> {
            e.printStackTrace();
            return false;
        });
    }

    public static List<String> fetchReply(FTPClient ftpClient) {
        return TGS_ListUtils.of(ftpClient.getReplyStrings());
    }

}
