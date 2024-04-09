package com.tugalsan.api.ftp.server;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_Union;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class TS_FtpUtils {

    final private static TS_Log d = TS_Log.of(TS_FtpUtils.class);

    public static TGS_Union<String> fetchWorkingDirectory(FTPClient ftpClient) {
        try {
            return TGS_Union.of(ftpClient.printWorkingDirectory());
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Boolean> makeDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        try {
            return TGS_Union.of(ftpClient.makeDirectory(newDir_withSlashPrefix.toString()));
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Boolean> upload(FTPClient ftpClient, Path file) {
        try {
            return TGS_Union.of(ftpClient.storeFile(TS_FileUtils.getNameFull(file), Files.newInputStream(file)));
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Boolean> changeWorkingDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        try {
            return TGS_Union.of(ftpClient.changeWorkingDirectory(newDir_withSlashPrefix.toString()));
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Boolean> destroy(FTPClient ftpClient) {
        try {
            ftpClient.logout();
        } catch (IOException ex) {
            //DO NOTHING
        }
        try {
            ftpClient.disconnect();
            return TGS_Union.of(true);
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<FTPClient> connect(CharSequence hostName, int port) {
        try {
            var ftpClient = new FTPClient();
            ftpClient.connect(hostName.toString(), port);
            var replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                TGS_Union.ofExcuse(d.className, "connect", "Operation failed. Server reply code: " + replyCode);
            }
            return TGS_Union.of(ftpClient);
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static TGS_Union<Boolean> login(FTPClient ftpClient, CharSequence user, CharSequence pass) {
        try {
            var success = ftpClient.login(user.toString(), pass.toString());
            if (true) {
                ftpClient.enterLocalPassiveMode();
            }
            return TGS_Union.of(success);
        } catch (IOException ex) {
            return TGS_Union.ofExcuse(ex);
        }
    }

    public static List<String> fetchReply(FTPClient ftpClient) {
        return TGS_ListUtils.of(ftpClient.getReplyStrings());
    }

}
