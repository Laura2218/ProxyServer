import java.util.*;

public class Cache {

    public static Map<String, cachedPage> cache = new HashMap<>();
    public static final int MAX_CACHE = 20;
    private static int length;

    public static Boolean insertCachePage(String url, cachedPage cp){
        if (length++<MAX_CACHE){
            cache.put(url, cp);
            return true;
        }
        else{
            return false;
        }
    }


    //这里将map.entrySet()转换成list
    List<Map.Entry<String,cachedPage>> list = new ArrayList<>(cache.entrySet());
    //然后通过比较器来实现排序

    Collections.sort(list, new Comparator<Map.Entry<String,cachedPage>> () {
        //升序排序
        public int compare(Map.Entry<String, cachedPage> o1, Entry<String, cachedPage> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    });
}
