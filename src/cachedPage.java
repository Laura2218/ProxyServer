
public class cachedPage{
    public String lastModified;
    public String page;
    public int addTime;
    public int visit;

    public cachedPage(String _lastModified, String _page, int addTime){
        this.lastModified = _lastModified;
        this.page = _page;
        visit = 0;
    }
}
