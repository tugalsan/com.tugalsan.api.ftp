package com.tugalsan.api.ftp.server;

import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.list.client.TGS_ListUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
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

    public static TGS_UnionExcuse makeDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        try {
            var result = ftpClient.makeDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuse.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuse.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuse upload(FTPClient ftpClient, Path file) {
        try {
            var result = ftpClient.storeFile(TS_FileUtils.getNameFull(file), Files.newInputStream(file));
            if (!result) {
                return TGS_UnionExcuse.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuse.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuse changeWorkingDirectory(FTPClient ftpClient, CharSequence newDir_withSlashPrefix) {
        try {
            var result = ftpClient.changeWorkingDirectory(newDir_withSlashPrefix.toString());
            if (!result) {
                return TGS_UnionExcuse.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuse.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static TGS_UnionExcuse destroy(FTPClient ftpClient) {
        try {
            ftpClient.logout();
        } catch (IOException ex) {
            //DO NOTHING
        }
        try {
            ftpClient.disconnect();
            return TGS_UnionExcuse.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
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

    public static TGS_UnionExcuse login(FTPClient ftpClient, CharSequence user, CharSequence pass) {
        try {
            var result = ftpClient.login(user.toString(), pass.toString());
            ftpClient.enterLocalPassiveMode();
            if (!result) {
                return TGS_UnionExcuse.ofExcuse(d.className, "makeDirectory", "result is false");
            }
            return TGS_UnionExcuse.ofVoid();
        } catch (IOException ex) {
            return TGS_UnionExcuse.ofExcuse(ex);
        }
    }

    public static List<String> fetchReply(FTPClient ftpClient) {
        return TGS_ListUtils.of(ftpClient.getReplyStrings());
    }

}
