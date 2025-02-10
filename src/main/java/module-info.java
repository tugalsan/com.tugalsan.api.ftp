module com.tugalsan.api.ftp {
    requires org.apache.commons.net;
    
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.function;
    
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.file;
    requires com.tugalsan.api.log;
    exports com.tugalsan.api.ftp.server;
}
