import java.io.*;

public class httpParser {

    // 将客户发送的报文内容copy到交付给目的主机的报文,并返回目的主机地址用于建立TCP连接
    public static void c2sMessage(InputStream from, OutputStream to, String addr) {
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(from));
        PrintStream out = new PrintStream(to);
        try {
            // 如果缓存的副本存在，则询问是否修改过
            if (Cache.cache.get(addr) != null) {
                out.println("if-modified-since:" + (Cache.cache.get(addr)).lastModified);
            }
            while (true) {
                line = br.readLine();
                if (line != null) {
                    out.println(line);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void s2cMessage(InputStream from, OutputStream to, String addr){
        BufferedReader br = new BufferedReader(new InputStreamReader(from));
        PrintStream out = new PrintStream(to);
        StringBuilder sb = new StringBuilder();
        String lastModifiedTime = "";
        String line;
        String[] lineSplit;
        Boolean data = false;
        while(true){
            try{
                line = br.readLine();
                lineSplit = line.split(":");
                if (lineSplit.length>1){
                    // 如果没有lastModified字段，使用Date字段的内容代替
                    if (lastModifiedTime.length()!=0 && lineSplit[0].startsWith("Date")){
                        lastModifiedTime = lineSplit[1];
                    }
                    if (lineSplit[0].startsWith("Last")){
                        lastModifiedTime = lineSplit[1];
                    }
                }
                out.println(line);
                if (data) sb.append(line);
                if (line != null) {
                    if (line.length() == 0){
                        out.println();
                        // 没有数据，则未更新，写入缓存中的内容
                        if ((line = br.readLine()) == null){
                            out.print((Cache.cache.get(addr)).page);
                            break;
                        }
                        else {
                            out.println(line);
                            sb.append(line);
                            data = true;
                        }
                    }
                }
                else{
                    Cache.insertCachePage(addr, new cachedPage(lastModifiedTime, sb.toString()));
                    break;
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}